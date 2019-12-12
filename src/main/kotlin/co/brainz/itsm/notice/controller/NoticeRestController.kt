package co.brainz.itsm.notice.controller

import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.notice.service.NoticeService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notices")
class NoticeRestController {

	companion object {
		private val logger = LoggerFactory.getLogger(NoticeRestController::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "NoticeRestController")
	}

	@Autowired
	lateinit var noticeRepository: NoticeRepository
	
	
    @Autowired
	lateinit var noticeService: NoticeService


	//Notice insert
	@RequestMapping(value = ["/", ""], method = [RequestMethod.POST])
	fun insertNotice(@RequestBody  notice: NoticeEntity){
		noticeRepository.save(notice)
	}
	
	//Notice update
	@RequestMapping(value = ["/{id}"], method = [RequestMethod.PUT])
	fun updateNotice(@RequestBody notice:NoticeEntity){
		noticeRepository.save(notice)
	}

	//Notice delete
	@RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
	fun deleteNotice(@PathVariable id: String) {
		noticeRepository.deleteById(id)
	}
	
	//공지사항 세부 조회
	@RequestMapping(value = [("/{id}")], method = [RequestMethod.GET])
	fun getNotice(@PathVariable id : String): NoticeEntity {
		return noticeService.findNoticeByNoticeNo(id)		
	}
	
	//공지사항 리스트 데이터 조회
	@RequestMapping(value = ["/",""], method = [RequestMethod.GET])
    fun getNoticeList(): List<NoticeEntity> {
		return noticeService.findNoticeList()
	}

}