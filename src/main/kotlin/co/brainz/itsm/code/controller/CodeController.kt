package co.brainz.itsm.code.controller

import co.brainz.itsm.user.service.UserService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/codes")
class CodeController(private val userService: UserService) {

    private val codeEditPage: String = "code/codeEdit"

    /**
     * 코드 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getCodeList(request: HttpServletRequest, model: Model): String {
        val allCodes = userService.getInitCodeList()
        model.addAttribute("langList", allCodes["langList"])
        return codeEditPage
    }
}
