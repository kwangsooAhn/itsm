package co.brainz.itsm.notice.service

import co.brainz.framework.fileTransaction.dto.FileDto
import co.brainz.framework.fileTransaction.service.FileService
import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.repository.NoticeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NoticeService(private val noticeRepository: NoticeRepository, private val fileService: FileService) {

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
        return noticeRepository.findAllByWriter(keyWord, fromDt, toDt)
    }

    fun findAllCheck(keyWord: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity> {
        return noticeRepository.findAllCheck(keyWord, fromDt, toDt)
    }

    fun findNoticeByNoticeNo(noticeNo: String): NoticeEntity {
        return noticeRepository.findById(noticeNo).orElse(NoticeEntity())
    }

    fun findNoticePopUp(): MutableList<NoticeEntity> {
        return noticeRepository.findNoticePopUp()
    }

    @Transactional
    fun insertNotice(noticeDto: NoticeDto) {
        val noticeEntity = NoticeEntity(
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
        val resltNoticeEntity = noticeRepository.save(noticeEntity)
        fileService.upload(FileDto(resltNoticeEntity.noticeNo, noticeDto.fileSeq))
    }

    @Transactional
    fun updateNotice(noticeDto: NoticeDto) {
        val noticeEntity = noticeRepository.findByNoticeNo(noticeDto.noticeNo)
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
        fileService.upload(FileDto(noticeEntity.noticeNo, noticeDto.fileSeq))
    }

    @Transactional
    fun delete(noticeNo: String) {
        noticeRepository.deleteById(noticeNo)
        fileService.delete(noticeNo)
    }

}
