package co.brainz.itsm.dashboard.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.dashboard.service.DashboardService
import co.brainz.itsm.token.service.TokenService
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * ### 대시보드 관련 뷰 화면 호출 처리용 클래스.
 * @author jy.lim
 */
@Controller
@RequestMapping("/dashboard")
class DashboardController(private val dashboardService: DashboardService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val dashboardViewPage: String = "dashboard/dashboardView"
    private val dashboardListPage: String = "dashboard/dashboardList"

    /**
     * Dashboard 화면 호출
     */
    @GetMapping("/view")
    fun getDashboardView(request: HttpServletRequest, model: Model): String {
        return dashboardViewPage
    }

    /**
     * Dashboard List 호출
     *
     * @param model
     * @return String
     */
    @GetMapping("/list")
    fun getDashboardList(model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params["userKey"] = aliceUserDto.userKey
        model.addAttribute("statusCountList", dashboardService.getStatusCountList(params))
        return dashboardListPage
    }
}
