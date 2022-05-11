/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessService
import co.brainz.workflow.process.service.WfProcessService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/process")
class ProcessController(
    private val processService: ProcessService,
    private val wfProcessService: WfProcessService
) {

    private val processDesignerEditPage: String = "process/processDesignerEdit"
    private val processImportPage: String = "process/processImport"

    /**
     * 프로세스 디자이너 편집 화면.
     */
    @GetMapping("/{processId}/edit")
    fun getProcessDesignerEdit(@PathVariable processId: String, model: Model): String {
        model.addAttribute("processId", processId)
        model.addAttribute("createdWorkFlow", wfProcessService.checkCreatedWorkFlow(processId))
        return processDesignerEditPage
    }

    /**
     * 프로세스 디자이너 보기 화면.
     */
    @GetMapping("/{processId}/view")
    fun getProcessDesignerView(@PathVariable processId: String, model: Model): String {
        model.addAttribute("processId", processId)
        model.addAttribute("isView", true)
        model.addAttribute("createdWorkFlow", wfProcessService.checkCreatedWorkFlow(processId))
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
