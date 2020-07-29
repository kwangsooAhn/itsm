package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/processes")
class ProcessController(private val processService: ProcessService) {

    private val processDesignerEditPage: String = "process/processDesignerEdit"
    private val processImportPage: String = "process/processImport"
    private val processStatusPage: String = "process/processStatus"

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

    /**
     * 프로세스 상태 화면.
     */
    @GetMapping("/{instanceId}/status")
    fun getProcessStatus(@PathVariable instanceId: String, model: Model): String {
        val processStatusDto = processService.getProcessStatus(instanceId)
        model.addAttribute("processStatus", processStatusDto)
        return processStatusPage
    }
}
