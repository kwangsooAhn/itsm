package co.brainz.itsm.notice.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.notice.repository.NoticeRepository
import org.springframework.web.bind.annotation.RequestParam
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import co.brainz.itsm.notice.entity.NoticeEntity
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestBody
import co.brainz.itsm.notice.service.NoticeService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping

@RestController
@RequestMapping("/notices")
class NoticeRestController(private val noticeRepository : NoticeRepository, private val noticeService : NoticeService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

	//Notice insert
    @PostMapping("/", "")
	fun insertNotice(@RequestBody  notice: NoticeEntity){
		noticeRepository.save(notice)
	}
	
	//Notice update
    @PutMapping("/{id}")
	fun updateNotice(@RequestBody notice:NoticeEntity){
		noticeRepository.save(notice)
	}

	//Notice delete
    @PostMapping("/{id}")
	fun deleteNotice(@PathVariable id: String) {
		noticeRepository.deleteById(id)
	}
	
	//공지사항 세부 조회
    @GetMapping("/{id}")
	fun getNotice(@PathVariable id : String): NoticeEntity {
		return noticeService.findNoticeByNoticeNo(id)		
	}
	
	//공지사항 리스트 데이터 조회
    @GetMapping("/", "")
    fun getNoticeList(): List<NoticeEntity> {
		return noticeService.findNoticeList()
	}

}