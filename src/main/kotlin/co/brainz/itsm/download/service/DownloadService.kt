/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.download.dto.DownloadDto
import co.brainz.itsm.download.dto.DownloadListReturnDto
import co.brainz.itsm.download.dto.DownloadSearchCondition
import co.brainz.itsm.download.mapper.DownloadMapper
import co.brainz.itsm.download.repository.DownloadRepository
import javax.transaction.Transactional
import kotlin.math.ceil
import org.mapstruct.factory.Mappers
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DownloadService(
    private val downloadRepository: DownloadRepository,
    private val aliceFileService: AliceFileService
) {

    private val downloadMapper: DownloadMapper = Mappers.getMapper(DownloadMapper::class.java)

    /**
     * [downloadSearchCondition]를 받아서 자료실 목록를 [List<DownloadListDto>] 반환한다.
     *
     */
    fun getDownloadList(downloadSearchCondition: DownloadSearchCondition): DownloadListReturnDto {
        val queryResult = downloadRepository.findDownloadEntityList(downloadSearchCondition)
        return DownloadListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = downloadRepository.count(),
                currentPageNum = downloadSearchCondition.pageNum,
                totalPageNum =
                    ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * 자료실 저장.
     *
     * @param downloadDto
     */
    @Transactional
    fun saveDownload(downloadDto: DownloadDto) {
        val downloadEntity = downloadRepository.save(downloadMapper.toDownloadEntity(downloadDto))
        aliceFileService.upload(
            AliceFileDto(
                ownId = downloadEntity.downloadId,
                fileSeq = downloadDto.fileSeqList,
                delFileSeq = downloadDto.delFileSeqList
            )
        )
    }

    /**
     * 자료실 상세 조회.
     *
     * @param downloadId
     * @param type
     * @return DownloadDto
     */
    @Transactional
    fun getDownloadDetail(downloadId: String, type: String): DownloadDto {
        var downloadEntity = downloadRepository.findDownload(downloadId)
        val sessionUser = SecurityContextHolder.getContext().authentication.principal as String
        if (type == "view" && downloadEntity.createUser?.userId != sessionUser) {
            downloadEntity.views += 1
            downloadEntity = downloadRepository.save(downloadEntity)
        }
        return downloadMapper.toDownloadDto(downloadEntity)
    }

    /**
     * 자료실 삭제.
     *
     * @param downloadId
     */
    @Transactional
    fun deleteDownload(downloadId: String) {
        downloadRepository.deleteById(downloadId)
        aliceFileService.delete(downloadId)
    }
}
