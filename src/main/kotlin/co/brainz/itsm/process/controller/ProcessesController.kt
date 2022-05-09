/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.itsm.process.controller

import co.brainz.itsm.process.dto.ProcessSearchCondition
import co.brainz.workflow.process.service.WfProcessService
import co.brainz.workflow.provider.constants.WorkflowConstants
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/processes")
class ProcessesController(
    private val wfProcessService: WfProcessService
) {

    private val processSearchPage: String = "process/processSearch"
    private val processListPage: String = "process/processList"

    /**
     * 프로세스 리스트 검색 호출 화면.
     */
    @GetMapping("/search")
    fun getProcessSearch(request: HttpServletRequest, model: Model): String {
        model.addAttribute("statusList", WorkflowConstants.ProcessStatus.values())
        return processSearchPage
    }

    /**
     * 프로세스 리스트 화면.
     */
    @GetMapping("")
    fun getProcessList(processSearchCondition: ProcessSearchCondition, model: Model): String {
        val result = wfProcessService.getProcesses(processSearchCondition)
        model.addAttribute("processList", result.data)
        model.addAttribute("paging", result.paging)
        return processListPage
    }
}
