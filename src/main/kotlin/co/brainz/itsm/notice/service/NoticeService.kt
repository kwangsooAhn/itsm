package co.brainz.itsm.notice.service

import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import co.brainz.itsm.notice.repository.NoticeRepository
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.notice.entity.NoticeEntity
import java.time.LocalDateTime

@Service
public open class NoticeService {
	
	companion object {
		private val logger  = LoggerFactory.getLogger(NoticeService::class.java)
	}
	
	fun Logging() : Unit {
		logger.info("INFO{ }", "NoticeSerivce")
	}
	
	@Autowired
    lateinit var noticeRepository : NoticeRepository
	

	public fun findNoticeList() : MutableList<NoticeEntity> {
		return noticeRepository.findAll()
	}

	public fun findTopNoticeList() : MutableList<NoticeEntity>{
		return noticeRepository.findTopNoticeList()
	}
	
	public fun findAllByTitle(keyword : String, fromDate : LocalDateTime, toDate : LocalDateTime) : MutableList<NoticeEntity>{
		return noticeRepository.findAllByTitle(keyword, fromDate, toDate)
	
	}
	
	public fun findAllByWriter(keyword : String, fromDate : LocalDateTime, toDate : LocalDateTime) : MutableList<NoticeEntity>{
		return noticeRepository.findAllByWriter(keyword,fromDate,toDate)
	}
	
	public fun findAllCheck(keyword : String, fromDate : LocalDateTime, toDate : LocalDateTime) : MutableList<NoticeEntity>{
		
		var copyKeyword : String = keyword
		
		return noticeRepository.findAllCheck(keyword, copyKeyword, fromDate, toDate)
	}
	
	public fun findNoticeByNoticeNo(noticeNo : String) : NoticeEntity{
		return noticeRepository.findById(noticeNo).orElse(NoticeEntity())
	}
	
	public fun findNoticePopUp() : MutableList<NoticeEntity>{
		return noticeRepository.findNoticePopUp()
	}
}