/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.service

import co.brainz.framework.auth.constants.AuthConstants
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.resourceManager.dto.AliceFileDto
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
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
import co.brainz.itsm.user.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import kotlin.math.ceil
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
    private val aliceResourceProvider: AliceResourceProvider,
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val noticeMapper: NoticeMapper = Mappers.getMapper(NoticeMapper::class.java)
    private val auth = AuthConstants.AuthType.PORTAL_MANAGE.value

    // 공지사항 검색 결과
    fun findNoticeSearch(noticeSearchCondition: NoticeSearchCondition): NoticeListReturnDto {
        val pagingResult = noticeRepository.findNoticeSearch(noticeSearchCondition)

        return NoticeListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = noticeRepository.count(),
                currentPageNum = noticeSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / noticeSearchCondition.contentNumPerPage).toLong(),
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
    fun insertNotice(noticeDto: NoticeDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val savedNotice = noticeRepository.save(
            NoticeEntity(
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
        )
        if (savedNotice.noticeNo.isNotEmpty()) {
            aliceResourceProvider.setUploadFileLoc(
                AliceFileDto(
                    ownId = savedNotice.noticeNo,
                    fileSeq = noticeDto.fileSeq,
                    delFileSeq = noticeDto.delFileSeq
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    @Transactional
    fun updateNotice(noticeNo: String, noticeDto: NoticeDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val noticeEntity = noticeRepository.findByNoticeNo(noticeNo)
        noticeEntity.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }
        if (noticeEntity.noticeNo.isNotEmpty()) {
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
            aliceResourceProvider.setUploadFileLoc(
                AliceFileDto(
                    ownId = noticeEntity.noticeNo,
                    fileSeq = noticeDto.fileSeq,
                    delFileSeq = noticeDto.delFileSeq
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    @Transactional
    fun delete(noticeNo: String): ZResponse {
        val noticeDto = noticeRepository.findById(noticeNo).get()
        noticeDto.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }
        var status = ZResponseConstants.STATUS.SUCCESS
        try {
            noticeRepository.deleteById(noticeNo)
            aliceResourceProvider.deleteByOwnId(noticeNo)
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
            e.printStackTrace()
        }
        return ZResponse(
            status = status.code
        )
    }
}
