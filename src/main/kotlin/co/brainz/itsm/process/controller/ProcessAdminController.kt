package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessAdminService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/processes-admin")
class ProcessAdminController(private val processAdminService: ProcessAdminService) {

    private val processSearchPage: String = "process/processAdminSearch"
    private val processListPage: String = "process/processAdminList"

    /**
     * 프로세스 리스트 검색 호출 화면.
     */
    @GetMapping("/search")
    fun getProcessSearch(request: HttpServletRequest, model: Model): String {
        return processSearchPage
    }

    /**
     * 프로세스 리스트 화면.
     */
    @GetMapping("/list")
    fun getProcessList(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = processAdminService.getProcesses(params)
        model.addAttribute("processList", result)
        model.addAttribute("processListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return processListPage
    }
}
