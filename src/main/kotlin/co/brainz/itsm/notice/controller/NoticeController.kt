package co.brainz.itsm.notice.controller  

import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.notice.service.NoticeService
import co.brainz.itsm.utility.ConvertParam
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/notices")
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

	//공지사항 리스트 화면
	@RequestMapping(value = ["/list"], method = [RequestMethod.GET])
	public fun getNoticeList(request: HttpServletRequest, model: Model): String {

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
		var addCurrentDate = LocalDateTime.now().plusDays(1)
		model.addAttribute("addCurrentDate",addCurrentDate)
		model.addAttribute("topNoticeList", noticeService.findTopNoticeList())
		return "notice/list"
	}
	
	//공지사항 조회 화면
	@RequestMapping(value = ["/notice"], method = [RequestMethod.GET])
	public fun getNotice(request: HttpServletRequest, model: Model): String {
		model.addAttribute("notice", noticeService.findNoticeByNoticeNo(request.getParameter("id")))
		return "notice/detail"
	}

	//공지사항 편집 화면
	@RequestMapping(value = ["/form"], method = [RequestMethod.GET])
	public fun getNoticeForm(@RequestParam(value = "id", defaultValue = "0") id: String, model: Model): String {
		var addCurrentDate = LocalDateTime.now().plusDays(1)
		model.addAttribute("addCurrentDate",addCurrentDate)
		model.addAttribute("notice", noticeService.findNoticeByNoticeNo(id))
		return "notice/form"
	}
	
	//공지사항 팝업 생성
	@RequestMapping(value = ["noticePopUp/{id}"], method = [RequestMethod.GET])
    public fun getNoticePopUp(@PathVariable id :String, model : Model) : String{
		model.addAttribute("isPopUp","true")
		model.addAttribute("noticePopUp", noticeService.findNoticeByNoticeNo(id))
		return "notice/noticePopUp"
	}
}