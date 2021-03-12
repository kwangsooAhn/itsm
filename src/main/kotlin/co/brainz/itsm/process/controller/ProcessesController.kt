/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.itsm.process.controller

import co.brainz.itsm.process.service.ProcessAdminService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/processes")
class ProcessesController(private val processAdminService: ProcessAdminService) {

    private val processSearchPage: String = "process/processSearch"
    private val processListPage: String = "process/processList"
    private val processListFragment: String = "process/processList :: list"

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
    @GetMapping("")
    fun getProcessList(
        request: HttpServletRequest,
        @RequestParam(value = "isScroll", required = false) isScroll: Boolean,
        model: Model
    ): String {
        val params = LinkedHashMap<String, Any>()
        params["search"] = request.getParameter("search")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = processAdminService.getProcesses(params)
        model.addAttribute("processList", result)
        model.addAttribute("processListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return if (isScroll) processListFragment else processListPage
    }
}
