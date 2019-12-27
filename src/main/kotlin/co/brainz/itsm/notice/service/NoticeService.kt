package co.brainz.itsm.notice.service

import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import co.brainz.itsm.notice.repository.NoticeRepository
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.notice.entity.NoticeEntity
import java.time.LocalDateTime

@Service
public open class NoticeService(private val noticeRepository : NoticeRepository) {
	
    private val logger = LoggerFactory.getLogger(this::class.java)

	public fun findNoticeList() : MutableList<NoticeEntity> {
		return noticeRepository.findAll()
	}

	public fun findTopNoticeList() : MutableList<NoticeEntity>{
		return noticeRepository.findTopNoticeList()
	}
	
	public fun findAllByTitle(keyword : String, fromDt : LocalDateTime, toDt : LocalDateTime) : MutableList<NoticeEntity>{
		return noticeRepository.findAllByTitle(keyword, fromDt, toDt)
	
	}
	
	public fun findAllByWriter(keyword : String, fromDt : LocalDateTime, toDt : LocalDateTime) : MutableList<NoticeEntity>{
		return noticeRepository.findAllByWriter(keyword,fromDt,toDt)
	}
	
	public fun findAllCheck(keyword : String, fromDt : LocalDateTime, toDt : LocalDateTime) : MutableList<NoticeEntity>{
		
		var copyKeyword : String = keyword
		
		return noticeRepository.findAllCheck(keyword, copyKeyword, fromDt, toDt)
	}
	
	public fun findNoticeByNoticeNo(noticeNo : String) : NoticeEntity{
		return noticeRepository.findById(noticeNo).orElse(NoticeEntity())
	}
	
	public fun findNoticePopUp() : MutableList<NoticeEntity>{
		return noticeRepository.findNoticePopUp()
	}
}