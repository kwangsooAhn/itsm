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
	
	@Autowired
	lateinit var convertParam : ConvertParam


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
}