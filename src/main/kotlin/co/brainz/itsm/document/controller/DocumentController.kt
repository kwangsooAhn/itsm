package co.brainz.itsm.document.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.UserConstants
import co.brainz.itsm.user.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/document")
class DocumentController(private val userService: UserService) {

    private val listPage: String = "document/list"
    private val formPage: String = "document/form"
    private val editPage: String = "document/edit"
    private val statusPage: String = "redirect:/certification/status"
    private val documentSearchPage: String = "document/documentSearch"

    @GetMapping("/list")
    fun getDocList(request: HttpServletRequest, model: Model): String {
        return listPage
    }

    @GetMapping("/form")
    fun getDocForm(request: HttpServletRequest, model: Model): String {
        //To-do 지원 가능한 언어 목록 가져오기

        //To-do 템플릿 정보 가져오기
        return formPage
    }

    @GetMapping("/edit")
    fun getDocEditor(request: HttpServletRequest, model: Model): String {
        //To-do 컴포넌트 상세 정보 가져오기
        return editPage
    }

    @GetMapping("/documentSearch")
    fun getDocumentSearch(request: HttpServletRequest, model: Model): String {
        //사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userKey = aliceUserDto.userKey
        val userDto: AliceUserEntity = userService.selectUserKey(userKey)
        if (userDto.status == UserConstants.Status.SIGNUP.code || userDto.status == UserConstants.Status.EDIT.code) {
            return statusPage
        }

        model.addAttribute("userKey", userKey)
        return documentSearchPage
    }
}
