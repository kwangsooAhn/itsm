/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticeListReturnDto
import co.brainz.itsm.notice.dto.NoticePopupDto
import co.brainz.itsm.notice.dto.NoticePopupListDto
import co.brainz.itsm.notice.dto.NoticeSearchCondition
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.mapper.NoticeMapper
import co.brainz.itsm.notice.repository.NoticeRepository
import kotlin.math.ceil
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeService(private val noticeRepository: NoticeRepository, private val aliceFileService: AliceFileService) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val noticeMapper: NoticeMapper = Mappers.getMapper(NoticeMapper::class.java)

    // 공지사항 검색 결과
    fun findNoticeSearch(noticeSearchCondition: NoticeSearchCondition): NoticeListReturnDto {
        val queryResult = noticeRepository.findNoticeSearch(noticeSearchCondition)

        return NoticeListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = noticeRepository.count(),
                currentPageNum = noticeSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / noticeSearchCondition.contentNumPerPage).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    // 공지사항 상단 리스트
    fun findTopNotice(): MutableList<NoticeListDto> {
        return noticeRepository.findTopNotice()
    }

    // 공지사항 조회 및 수정용 세부정보
    fun findNoticeByNoticeNo(noticeNo: String): NoticeDto {
        return noticeMapper.toNoticeDto(noticeRepository.findNotice(noticeNo))
    }

    // 최초 로그인 시 팝업용 공지사항 조회
    fun findPopupNoticeByNoticeNo(noticeNo: String): NoticePopupDto {
        return noticeMapper.toNoticePopupDto(noticeRepository.findNotice(noticeNo))
    }

    // 최초 로그인 시 팝업용 공지사항 리스트 조회
    fun findNoticePopUp(): MutableList<NoticePopupListDto> {
        val noticePopupListDto = mutableListOf<NoticePopupListDto>()
        noticeRepository.findNoticePopUp().forEach { noticeEntity ->
            noticePopupListDto.add(noticeMapper.toNoticePopupListDto(noticeEntity))
        }
        return noticePopupListDto
    }

    @Transactional
    fun insertNotice(noticeDto: NoticeDto): Boolean {
        var isSuccess = true
        val noticeToSave = NoticeEntity(
            noticeDto.noticeNo,
            noticeDto.noticeTitle,
            noticeDto.noticeContents,
            noticeDto.popYn,
            noticeDto.popStrtDt,
            noticeDto.popEndDt,
            noticeDto.popWidth,
            noticeDto.popHeight,
            noticeDto.topNoticeYn,
            noticeDto.topNoticeStrtDt,
            noticeDto.topNoticeEndDt
        )
        val savedNotice = noticeRepository.save(noticeToSave)
        if (savedNotice.noticeNo.isNotEmpty()) {
            aliceFileService.upload(
                AliceFileDto(
                    ownId = savedNotice.noticeNo,
                    fileSeq = noticeDto.fileSeq,
                    delFileSeq = noticeDto.delFileSeq
                )
            )
        } else {
            isSuccess = false
        }
        return isSuccess
    }

    @Transactional
    fun updateNotice(noticeId: String, noticeDto: NoticeDto): Boolean {
        val noticeEntity = noticeRepository.findByNoticeNo(noticeId)
        noticeEntity.noticeTitle = noticeDto.noticeTitle
        noticeEntity.noticeContents = noticeDto.noticeContents
        noticeEntity.popStrtDt = noticeDto.popStrtDt
        noticeEntity.popEndDt = noticeDto.popEndDt
        noticeEntity.popWidth = noticeDto.popWidth
        noticeEntity.popHeight = noticeDto.popHeight
        noticeEntity.popYn = noticeDto.popYn
        noticeEntity.topNoticeStrtDt = noticeDto.topNoticeStrtDt
        noticeEntity.topNoticeEndDt = noticeDto.topNoticeEndDt
        noticeEntity.topNoticeYn = noticeDto.topNoticeYn
        noticeRepository.save(noticeEntity)
        aliceFileService.upload(
            AliceFileDto(
                ownId = noticeEntity.noticeNo,
                fileSeq = noticeDto.fileSeq,
                delFileSeq = noticeDto.delFileSeq
            )
        )
        return true
    }

    @Transactional
    fun delete(noticeNo: String): Boolean {
        noticeRepository.deleteById(noticeNo)
        aliceFileService.delete(noticeNo)
        return true
    }
}
