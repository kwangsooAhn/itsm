package co.brainz.itsm.document.controller

import co.brainz.itsm.certification.constants.CertificationConstants
import co.brainz.itsm.user.UserEntity
import co.brainz.itsm.user.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/document")
class DocumentController(private val userService: UserService) {
    
    @GetMapping("/list")
    fun getDocList(request: HttpServletRequest, model: Model): String {
        return "document/list"
    }
    
    @GetMapping("/form")
    fun getDocForm(request: HttpServletRequest, model: Model): String {
        //To-do 지원 가능한 언어 목록 가져오기
        
        //To-do 템플릿 정보 가져오기
        return "document/form"
    }
    
    @GetMapping("/edit")
    fun getDocEditor(request: HttpServletRequest, model: Model): String {
        //To-do 컴포넌트 상세 정보 가져오기
        return "document/edit"
    }
    
    @GetMapping("/documentSearch")
    fun getDocumentSearch(request: HttpServletRequest, model: Model): String {
        //사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: UserEntity = userService.selectUser(userId)

        if (userDto.status == CertificationConstants.UserStatus.SIGNUP.code) {
            return "redirect:/certification/status"
        }
        return "document/documentSearch"
    }
}
