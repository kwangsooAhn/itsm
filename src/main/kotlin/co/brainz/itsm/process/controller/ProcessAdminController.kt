package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessAdminService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/processes-admin")
class ProcessAdminController(private val processAdminService: ProcessAdminService) {

    private val processSearchPage: String = "process/processAdminSearch"
    private val processListPage: String = "process/processAdminList"
    private val processEditPage: String = "process/processAdminEdit"
    private val processViewPage: String = "process/processAdminView"

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
        model.addAttribute("processList", processAdminService.getProcesses(params))
        return processListPage
    }

    /**
     * 프로세스 기본 정보 등록 화면.
     */
    @GetMapping("/new")
    fun getProcessNew(request: HttpServletRequest, model: Model): String {
        // TODO 템플릿 정보 가져오기
        return processEditPage
    }

    /**
     * 프로세스 기본 정보 수정 화면.
     */
    @GetMapping("/{processId}/edit")
    fun getProcessEdit(@PathVariable processId: String, model: Model): String {
        model.addAttribute("processInfo", processAdminService.getProcessAdmin(processId))
        return processEditPage
    }

    /**
     * 프로세스 기본 정보 조회 화면.
     */
    @GetMapping("/{processId}/view")
    fun getProcessView(@PathVariable processId: String, model: Model): String {
        model.addAttribute("processId", processId)
        model.addAttribute("isView", true)
        return processViewPage
    }
}
