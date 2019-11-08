package co.brainz.itsm.notice.service

import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import co.brainz.itsm.notice.repository.NoticeRepository
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.notice.domain.Notice

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
	
	// 공지사항 전체 게시물
	public fun findNoticeList() : MutableList<Notice> {
		return noticeRepository.findAll()
	}
	// 상단 설정 공지사항 게시물
	public fun findTopNoticeList() : MutableList<Notice>{
		return noticeRepository.findTopNoticeList()
	}
	
}