/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.archive.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.archive.dto.ArchiveDto
import co.brainz.itsm.archive.dto.ArchiveListReturnDto
import co.brainz.itsm.archive.dto.ArchiveSearchCondition
import co.brainz.itsm.archive.mapper.ArchiveMapper
import co.brainz.itsm.archive.repository.ArchiveRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import javax.transaction.Transactional
import kotlin.math.ceil
import org.mapstruct.factory.Mappers
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class ArchiveService(
    private val archiveRepository: ArchiveRepository,
    private val aliceFileService: AliceFileService
) {

    private val archiveMapper: ArchiveMapper = Mappers.getMapper(ArchiveMapper::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * [archiveSearchCondition]를 받아서 자료실 목록를 [List<ArchiveListDto>] 반환한다.
     *
     */
    fun getArchiveList(archiveSearchCondition: ArchiveSearchCondition): ArchiveListReturnDto {
        val pagingResult = archiveRepository.findArchiveEntityList(archiveSearchCondition)
        return ArchiveListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = archiveRepository.count(),
                currentPageNum = archiveSearchCondition.pageNum,
                totalPageNum =
                ceil(pagingResult.totalCount.toDouble() / archiveSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * 자료실 저장.
     *
     * @param archiveDto
     */
    @Transactional
    fun saveArchive(archiveDto: ArchiveDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val archiveEntity = archiveRepository.save(archiveMapper.toArchiveEntity(archiveDto))
        if (archiveEntity.archiveId.isNotEmpty()) {
            aliceFileService.upload(
                AliceFileDto(
                    ownId = archiveEntity.archiveId,
                    fileSeq = archiveDto.fileSeqList,
                    delFileSeq = archiveDto.delFileSeqList
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 자료실 상세 조회.
     *
     * @param archiveId
     * @param type
     * @return ArchiveDto
     */
    @Transactional
    fun getArchiveDetail(archiveId: String, type: String): ArchiveDto {
        var archiveEntity = archiveRepository.findArchive(archiveId)
        val sessionUser = SecurityContextHolder.getContext().authentication.principal as String
        if (type == "view" && archiveEntity.createUser?.userId != sessionUser) {
            archiveEntity.views += 1
            archiveEntity = archiveRepository.save(archiveEntity)
        }
        return archiveMapper.toArchiveDto(archiveEntity)
    }

    /**
     * 자료실 삭제.
     *
     * @param archiveId
     */
    @Transactional
    fun deleteArchive(archiveId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        try {
            archiveRepository.deleteById(archiveId)
            aliceFileService.delete(archiveId)
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
            e.printStackTrace()
        }
        return ZResponse(
            status = status.code
        )
    }
}
