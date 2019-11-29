package co.brainz.itsm.notice.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.ui.Model
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.notice.service.NoticeService
import org.slf4j.LoggerFactory

@Controller
class NoticePopUpController {

	companion object {
		private val logger = LoggerFactory.getLogger(NoticePopUpController::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "NoticePopUpController")

	}


	@Autowired
	lateinit var noticeService: NoticeService

	@GetMapping("/index2")
	fun noticePopUp(model: Model): String {

		model.addAttribute("noticePopUp", noticeService.findNoticePopUp())

		return "index2"
	}

	@GetMapping("/noticePopUp/{id}")
	public fun getNoticePopUP(@PathVariable id: String, model: Model): String {

		model.addAttribute("noticePopUp", noticeService.findNoticeByNoticeNo(id))

		return "notice/noticePopUp"
	}
}