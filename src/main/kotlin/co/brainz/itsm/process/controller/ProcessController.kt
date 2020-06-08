package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/processes")
class ProcessController(private val processService: ProcessService) {

    private val processSearchPage: String = "process/processSearch"
    private val processListPage: String = "process/processList"
    private val processEditPage: String = "process/processEdit"
    private val processDesignerEditPage: String = "process/processDesignerEdit"
    private val processImportPage: String = "process/processImport"

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
        model.addAttribute("processList", processService.getProcesses(params))
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
     * 프로세스 디자이너 편집 화면.
     */
    @GetMapping("/{processId}/edit")
    fun getProcessDesignerEdit(@PathVariable processId: String, model: Model): String {
        model.addAttribute("processId", processId)
        return processDesignerEditPage
    }

    /**
     * 프로세스 디자이너 보기 화면.
     */
    @GetMapping("/{processId}/view")
    fun getProcessDesignerView(@PathVariable processId: String, model: Model): String {
        model.addAttribute("processId", processId)
        model.addAttribute("isView", true)
        return processDesignerEditPage
    }

    /**
     * 프로세스 Import 화면.
     */
    @GetMapping("/import")
    fun getProcessImport(request: HttpServletRequest, model: Model): String {
        return processImportPage
    }
}
