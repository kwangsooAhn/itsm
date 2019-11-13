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
	lateinit var noticeService: NoticeService


	@GetMapping("", "/")
	public fun list(request: HttpServletRequest, model: Model): String {
				
		var dateFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
		var dateFormatterAppend : DateTimeFormatter  = DateTimeFormatterBuilder().append(dateFormatter).parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter()
	
		if (!(request.getParameter("notice_title") == "check" && request.getParameter("create_userId") == "check")) {
			
			if (request.getParameter("notice_title") == "check") {
			    var fromDate : LocalDateTime = LocalDateTime.parse(request.getParameter("fromDate"), dateFormatterAppend)
	            var toDate : LocalDateTime = LocalDateTime.parse(request.getParameter("toDate"), dateFormatterAppend)
				model.addAttribute("noticeList", noticeService.findAllByTitle(request.getParameter("keyword"),fromDate,toDate))
			} else if (request.getParameter("create_userId") == "check") {
				var fromDate : LocalDateTime = LocalDateTime.parse(request.getParameter("fromDate"), dateFormatterAppend)
	            var toDate : LocalDateTime = LocalDateTime.parse(request.getParameter("toDate"), dateFormatterAppend)
				model.addAttribute("noticeList", noticeService.findAllByWriter(request.getParameter("keyword"),fromDate,toDate))
			} else {
				
				model.addAttribute("noticeList", noticeService.findNoticeList())
			}
		} else if (request.getParameter("notice_title") == "check" && request.getParameter("create_userId") == "check") {
			var fromDate : LocalDateTime = LocalDateTime.parse(request.getParameter("fromDate"), dateFormatterAppend)
	        var toDate : LocalDateTime = LocalDateTime.parse(request.getParameter("toDate"), dateFormatterAppend)
			model.addAttribute("noticeList", noticeService.findAllCheck(request.getParameter("keyword"),fromDate,toDate))
		}
		model.addAttribute("topNoticeList", noticeService.findTopNoticeList())
		return "notice/list"
	}


}