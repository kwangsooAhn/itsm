package co.brainz.itsm.notice.service

import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.repository.NoticeRepository
import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

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
}