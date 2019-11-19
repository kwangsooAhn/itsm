package co.brainz.itsm.notice.service

import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import co.brainz.itsm.notice.repository.NoticeRepository
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.notice.domain.Notice
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
	

	public fun findNoticeList() : MutableList<Notice> {
		return noticeRepository.findAll()
	}

	public fun findTopNoticeList() : MutableList<Notice>{
		return noticeRepository.findTopNoticeList()
	}
	
	public fun findAllByTitle(keyword : String, fromDate : LocalDateTime, toDate : LocalDateTime) : MutableList<Notice>{
		return noticeRepository.findAllByTitle(keyword, fromDate, toDate)
	
	}
	
	public fun findAllByWriter(keyword : String, fromDate : LocalDateTime, toDate : LocalDateTime) : MutableList<Notice>{
		return noticeRepository.findAllByWriter(keyword,fromDate,toDate)
	}
	
	public fun findAllCheck(keyword : String, fromDate : LocalDateTime, toDate : LocalDateTime) : MutableList<Notice>{
		
		var copyKeyword : String = keyword
		
		return noticeRepository.findAllCheck(keyword, copyKeyword, fromDate, toDate)
	}
	
	public fun findNoticeByNoticeNo(noticeNo : String) : Notice{
		return noticeRepository.findById(noticeNo).orElse(Notice())
	}
	
	
	
	

}