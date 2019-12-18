package co.brainz.itsm.process

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/process")
class ProcessController {
    @RequestMapping(path = ["/list"], method = [RequestMethod.GET])
    fun getProcessList(request: HttpServletRequest, model: Model): String {
        return "process/list"
    }

    @RequestMapping(path = ["/edit"], method = [RequestMethod.GET])
    fun getEditor(@RequestParam(value="processId", defaultValue="") processId: String, model: Model): String {
        return "process/edit"
    }
}