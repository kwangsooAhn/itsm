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
	lateinit var noticeService : NoticeService

	
	@GetMapping("","/")
	public fun list(request : HttpServletRequest, model: Model): String {
		System.out.println("request" + request)
		System.out.println("test2 "+request.getParameter("keyword"))
	
		// 전체 공지사항 게시물
		model.addAttribute("noticeList", noticeService.findNoticeList())
		// 상단 설정 공지사항 게시물
		model.addAttribute("topNoticeList",noticeService.findTopNoticeList())

		return "notice/list"
	}


}