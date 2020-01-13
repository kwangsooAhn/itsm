package co.brainz.itsm.notice.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.repository.NoticeRepository
import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder

@Service
class NoticeService(private val noticeRepository: NoticeRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findNoticeList(): MutableList<NoticeEntity> {
        return noticeRepository.findAll()
    }

    fun findTopNoticeList(): MutableList<NoticeEntity> {
        return noticeRepository.findTopNoticeList()
    }

    fun findAllByTitle(keyWord: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity> {
        return noticeRepository.findAllByTitle(keyWord, fromDt, toDt)
    }

    fun findAllByWriter(keyWord: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity> {
        return noticeRepository.findAllByWriter(keyWord,fromDt,toDt)
    }

    fun findAllCheck(keyWord: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity> {
        return noticeRepository.findAllCheck(keyWord, fromDt, toDt)
    }

    fun findNoticeByNoticeNo(noticeNo : String): NoticeEntity {
        return noticeRepository.findById(noticeNo).orElse(NoticeEntity())
    }

    fun findNoticePopUp(): MutableList<NoticeEntity> {
        return noticeRepository.findNoticePopUp()
    }

    fun insertNotice(notice: NoticeEntity) {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        notice.createUserkey = aliceUserDto.userKey
        noticeRepository.save(notice)
    }

    fun updateNotice(notice: NoticeEntity) {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val noticeEntity = noticeRepository.findByNoticeNo(notice.noticeNo)
        noticeEntity.updateDt = LocalDateTime.now()
        noticeEntity.updateUserkey = aliceUserDto.userKey
        noticeEntity.noticeTitle = notice.noticeTitle
        noticeEntity.noticeContents = notice.noticeContents
        noticeEntity.popStrtDt = notice.popStrtDt
        noticeEntity.popEndDt = notice.popEndDt
        noticeEntity.popWidth = notice.popWidth
        noticeEntity.popHeight = notice.popHeight
        noticeEntity.popYn = notice.popYn
        noticeEntity.topNoticeStrtDt = notice.topNoticeStrtDt
        noticeEntity.topNoticeEndDt = notice.topNoticeEndDt
        noticeEntity.topNoticeYn = notice.topNoticeYn
        noticeRepository.save(noticeEntity)
    }

}
