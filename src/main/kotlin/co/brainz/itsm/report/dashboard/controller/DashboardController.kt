package co.brainz.itsm.report.dashboard.controller

import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/dashboards")
class DashboardController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val dashboardTemplateSearch: String = "report/dashboardTemplateSearch"
    private val dashboardManagement: String = "report/dashboardManagementEdit"

    @GetMapping("/dashboardTemplate/search")
    fun getDashboardTemplateSearch(request: HttpServletRequest, model: Model): String {
        return dashboardTemplateSearch
    }

    @GetMapping("/dashboardManagement/edit")
    fun getDashboardManagementList(request: HttpServletRequest, model: Model): String {
        return dashboardManagement
    }
}