package co.brainz.itsm.notice.controller  

import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.notice.service.NoticeService
import co.brainz.itsm.utility.ConvertParam
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import co.brainz.itsm.user.UserService
import org.springframework.security.core.context.SecurityContextHolder
import co.brainz.itsm.user.UserEntity
import co.brainz.itsm.certification.UserStatus


@Controller
public class NoticeController(private val noticeRepository : NoticeRepository, private val noticeService : NoticeService, private val convertParam : ConvertParam, private val userService : UserService) {
	
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("notices/list")
    public fun getNoticeList(request: HttpServletRequest, model: Model) : String {
        var addCurrentDate = LocalDateTime.now().plusDays(1)
        model.addAttribute("addCurrentDate",addCurrentDate)
        return "notice/list"
    }

    @GetMapping("/notices/ajaxList")
	public fun getNoticeSearchList(request: HttpServletRequest, model: Model): String {

		if (!(request.getParameter("noticeTitle") == "true" && request.getParameter("createUserid") == "true")) {
		    if (request.getParameter("noticeTitle") == "true") {
			    var fromDt: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDt"), "fromDt")
				var toDt: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDt"), "toDt")
				model.addAttribute("noticeList", noticeService.findAllByTitle(request.getParameter("keyword"), fromDt, toDt)
				)
			} else if (request.getParameter("createUserid") == "true") {
				var fromDt: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDt"), "fromDt")
				var toDt: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDt"), "toDt")
				model.addAttribute("noticeList", noticeService.findAllByWriter(request.getParameter("keyword"), fromDt, toDt)
				)
			} else {
				model.addAttribute("noticeList", noticeService.findNoticeList())
			}
		} else if (request.getParameter("noticeTitle") == "true" && request.getParameter("createUserid") == "true") {
			var fromDt: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDt"), "fromDt")
			var toDt: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDt"), "toDt")
			model.addAttribute( "noticeList", noticeService.findAllCheck(request.getParameter("keyword"), fromDt, toDt)
			)
		}
		var addCurrentDate = LocalDateTime.now().plusDays(1)
		model.addAttribute("addCurrentDate",addCurrentDate)
		model.addAttribute("topNoticeList", noticeService.findTopNoticeList())
		return "notice/ajaxList"
	}
	
	//공지사항 조회 화면
    @GetMapping("/notices/notice")
	public fun getNotice(request: HttpServletRequest, model: Model): String {
		model.addAttribute("notice", noticeService.findNoticeByNoticeNo(request.getParameter("id")))
		return "notice/detail"
	}

	//공지사항 편집 화면
    @GetMapping("/notices/form")
	public fun getNoticeForm(@RequestParam(value = "id", defaultValue = "0") id: String, model: Model): String {
		var addCurrentDate = LocalDateTime.now().plusDays(1)
		model.addAttribute("addCurrentDate",addCurrentDate)
		model.addAttribute("notice", noticeService.findNoticeByNoticeNo(id))
		return "notice/form"
	}
	
	//공지사항 팝업 생성
    @GetMapping("/notices/noticePopUp/{id}")
    public fun getNoticePopUp(@PathVariable id :String, @RequestParam(required=false) value : String?, model : Model) : String{
		
        if(value == "true"){
        model.addAttribute("isPopUp","true")
        }
 
		model.addAttribute("noticePopUp", noticeService.findNoticeByNoticeNo(id))
		return "notice/noticePopUp"
	}
    
    @GetMapping("/index2")
    fun noticePopUp(model: Model): String {

        model.addAttribute("noticePopUp", noticeService.findNoticePopUp())

        //사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: UserEntity = userService.selectUser(userId)
        if (userDto.status == UserStatus.SIGNUP.code) {
            return "redirect:/certification/status"
        }
        return "index2"
    }
}