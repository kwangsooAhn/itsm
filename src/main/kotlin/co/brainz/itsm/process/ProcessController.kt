package co.brainz.itsm.process

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/process")
class ProcessController {

    @GetMapping("/list")
    fun getProcessList(request: HttpServletRequest, model: Model) = "process/list"

    @GetMapping("/edit")
    fun getEditor(@RequestParam(value="processId", defaultValue="") processId: String) = "process/edit"

}