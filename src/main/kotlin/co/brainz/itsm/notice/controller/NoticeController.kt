package co.brainz.itsm.notice.controller

import org.springframework.web.bind.annotation.RestController
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.Resource
import co.brainz.itsm.notice.service.NoticeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import co.brainz.itsm.utility.ConvertParam
import org.springframework.web.bind.annotation.PathVariable
import co.brainz.itsm.notice.repository.NoticeRepository
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestBody
import co.brainz.itsm.notice.domain.Notice
import org.springframework.web.bind.annotation.PostMapping


@Controller
@RequestMapping("/notice")
public class NoticeController {


	companion object {
		private val logger = LoggerFactory.getLogger(NoticeController::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "NoticeController")

	}
	


    @Autowired
    lateinit var noticeRepository : NoticeRepository
	
	@Autowired
	lateinit var noticeService: NoticeService
	
	@Autowired
	lateinit var convertParam : ConvertParam
	
	//list º¸±â
	@GetMapping("", "/")
	public fun list(request: HttpServletRequest, model: Model): String {

		if (!(request.getParameter("notice_title") == "check" && request.getParameter("create_userId") == "check")) {
			if (request.getParameter("notice_title") == "check") {
			    var fromDate : LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDate"),"fromDate")
	            var toDate : LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDate"),"toDate")
				model.addAttribute("noticeList", noticeService.findAllByTitle(request.getParameter("keyword"),fromDate,toDate))
			} else if (request.getParameter("create_userId") == "check") {
			    var fromDate : LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDate"),"fromDate")
	            var toDate : LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDate"),"toDate")
				model.addAttribute("noticeList", noticeService.findAllByWriter(request.getParameter("keyword"),fromDate,toDate))
			} else {
				model.addAttribute("noticeList", noticeService.findNoticeList())
			}
		} else if (request.getParameter("notice_title") == "check" && request.getParameter("create_userId") == "check") {
			var fromDate : LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDate"),"fromDate")
	        var toDate : LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDate"),"toDate")
			model.addAttribute("noticeList", noticeService.findAllCheck(request.getParameter("keyword"),fromDate,toDate))
		}
		model.addAttribute("topNoticeList", noticeService.findTopNoticeList())
		return "notice/list"
	}
	
	@GetMapping("/detail")
	public fun detail(@RequestParam(value = "noticeNo") noticeNo : String, model:Model):String {
		model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeNo))
		return "notice/detail"
	}
	
    @GetMapping("/form")
    public fun form(@RequestParam(value = "noticeNo", defaultValue = "0") noticeNo : String, model:Model):String {
		model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeNo))
		return "notice/form"
	}
	
	// delete
	@RequestMapping("/delete/{noticeNo}")
	public fun delete(@PathVariable noticeNo : String) : String{
		noticeRepository.deleteById(noticeNo);
		return "redirect:/notice";	
	}
	
	//insert
	@RequestMapping(value = ["/insert"] ,method = [RequestMethod.POST])
    public fun insert(notice : Notice) : String {
		noticeRepository.save(notice)
		return "redirect:/notice";	
	}
	
	//update
	

}