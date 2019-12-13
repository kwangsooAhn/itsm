package co.brainz.itsm.notice.controller

import co.brainz.itsm.certification.CertificationEnum
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.ui.Model
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.notice.service.NoticeService
import co.brainz.itsm.settings.user.UserEntity
import co.brainz.itsm.settings.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder

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

	@Autowired
	lateinit var userService: UserService

	@GetMapping("/index2")
	fun noticePopUp(model: Model): String {

		model.addAttribute("noticePopUp", noticeService.findNoticePopUp())

		//사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
		val userId: String = SecurityContextHolder.getContext().authentication.principal as String
		val userDto: UserEntity = userService.selectUser(userId)
		if (userDto.status == CertificationEnum.SIGNUP.code) {
			return "redirect:/certification/status"
		}
		return "index2"
	}

	@GetMapping("/noticePopUp/{id}")
	public fun getNoticePopUp(@PathVariable id: String, model: Model): String {

		model.addAttribute("noticePopUp", noticeService.findNoticeByNoticeNo(id))

		return "notice/noticePopUp"
	}
}