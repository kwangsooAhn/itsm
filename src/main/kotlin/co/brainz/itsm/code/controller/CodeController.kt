package co.brainz.itsm.code.controller

import co.brainz.itsm.code.service.CodeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/codes")
class CodeController(private val codeService: CodeService) {

    private val codeEditPage: String = "code/codeEdit"

    /**
     * 코드 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getCodeList(request: HttpServletRequest, model: Model): String {
        model.addAttribute("codeList", codeService.getCodeList())
        return codeEditPage
    }
}