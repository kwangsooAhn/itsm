package co.brainz.itsm.role.controller

import co.brainz.itsm.role.service.RoleService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/roles")
@Controller
class RoleController(private val roleService: RoleService) {

    private val logger = LoggerFactory.getLogger(RoleController::class.java)
    private val roleEditPage: String = "role/roleEdit"

    /**
     * 역할  설정 뷰를 호출한다.
     */
    @GetMapping("/edit", "")
    fun getRoleList(request: HttpServletRequest, model: Model): String {

        val roleAllList = roleService.selectRoleList()
        val authAllList = roleService.selectAuthList()
        model.addAttribute("authList", authAllList)
        model.addAttribute("roleList", roleAllList)

        return roleEditPage
    }
}
