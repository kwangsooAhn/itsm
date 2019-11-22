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
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.ui.ModelMap


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
	lateinit var noticeRepository: NoticeRepository

	@Autowired
	lateinit var noticeService: NoticeService

	@Autowired
	lateinit var convertParam: ConvertParam

	//list
	@GetMapping("", "/")
	public fun list(request: HttpServletRequest, model: Model): String {
		println("list controller")

		if (!(request.getParameter("notice_title") == "check" && request.getParameter("create_userId") == "check")) {
			if (request.getParameter("notice_title") == "check") {
				var fromDate: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDate"), "fromDate")
				var toDate: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDate"), "toDate")
				model.addAttribute("noticeList", noticeService.findAllByTitle(request.getParameter("keyword"), fromDate, toDate)
				)
			} else if (request.getParameter("create_userId") == "check") {
				var fromDate: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDate"), "fromDate")
				var toDate: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDate"), "toDate")
				model.addAttribute( "noticeList", noticeService.findAllByWriter(request.getParameter("keyword"), fromDate, toDate)
				)
			} else {
				model.addAttribute("noticeList", noticeService.findNoticeList())
			}
		} else if (request.getParameter("notice_title") == "check" && request.getParameter("create_userId") == "check") {
			var fromDate: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDate"), "fromDate")
			var toDate: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDate"), "toDate")
			model.addAttribute( "noticeList", noticeService.findAllCheck(request.getParameter("keyword"), fromDate, toDate)
			)
		}
		var addCurrentDate = LocalDateTime.now().plusDays(6)
		model.addAttribute("addCurrentDate",addCurrentDate)
		model.addAttribute("topNoticeList", noticeService.findTopNoticeList())
		return "notice/list"
	}

	@GetMapping("/detail")
	public fun detail(@RequestParam(value = "noticeNo") noticeNo: String, model: Model): String {
		model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeNo))
		return "notice/detail"
	}

	@GetMapping("/form")
	public fun form(@RequestParam(value = "noticeNo", defaultValue = "0") noticeNo: String, model: Model): String {
		
		var addCurrentDate = LocalDateTime.now().plusDays(6)
		model.addAttribute("addCurrentDate",addCurrentDate)
		
		model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeNo))
		return "notice/form"
	}

	// delete
	@RequestMapping("/delete/{noticeNo}")
	public fun delete(@PathVariable noticeNo: String): String {
		noticeRepository.deleteById(noticeNo);
		return "redirect:/notice";
	}

	//insert
	@RequestMapping(value = ["/edit/{noticeNo}","/edit"], method = [RequestMethod.POST])
	public fun edit(
		@RequestParam (required = false) popStrtDtBefore :String,
		@RequestParam (required = false) popEndDtBefore:String,
		@RequestParam (required = false) topNoticeStrtDtBefore:String,
		@RequestParam (required = false) topNoticeEndDtBefore:String,
		@RequestParam (required = false) popYn : String,
		@RequestParam (required = false) topNoticeYn : String,
	    @RequestParam createDtBefore:String,
		notice: Notice): String {
		
		var formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	    var createDtAfter : LocalDateTime = LocalDateTime.parse(createDtBefore,formatter)
		
		if(popYn == "false"){
		notice.popStrtDt = null
		notice.popEndDt = null
		notice.popWidth = null
		notice.popHeight = null
		}else{
		var popStrtDtAfter : LocalDateTime? = LocalDateTime.parse(popStrtDtBefore,formatter)
		var popEndDtAfter : LocalDateTime? = LocalDateTime.parse(popEndDtBefore,formatter)
		notice.popStrtDt = popStrtDtAfter
		notice.popEndDt = popEndDtAfter
		}
		
		if(topNoticeYn == "false"){
		notice.topNoticeStrtDt = null
		notice.topNoticeEndDt = null
		}else{
		var topNoticeStrtDtAfter : LocalDateTime? = LocalDateTime.parse(topNoticeStrtDtBefore,formatter)
		var topNoticeEndDtAfter : LocalDateTime? = LocalDateTime.parse(topNoticeEndDtBefore,formatter)
		notice.topNoticeStrtDt = topNoticeStrtDtAfter
		notice.topNoticeEndDt = topNoticeEndDtAfter		
		}	
			
		notice.createDt = createDtAfter
		
		noticeRepository.save(notice)
		
		return "redirect:/notice";
	}

}