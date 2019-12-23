package co.brainz.itsm.role

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.ui.Model
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.time.LocalDateTime
import co.brainz.itsm.role.RoleService

@RequestMapping("/roles")
@Controller
public class RoleController {

    private val logger = LoggerFactory.getLogger(RoleController::class.java)

    var roleService: RoleService

    constructor(roleService: RoleService) {
        this.roleService = roleService
    }

    /**
     * 역할  설정 뷰를 호출한다.
     */
    @GetMapping("/form", "")
    public fun getRolelist(request: HttpServletRequest, model: Model): String {

        var roleAllList = roleService.selectRoleList()
        var authAllList = roleService.selectAuthList()
        model.addAttribute("authList", authAllList)
        model.addAttribute("roleList", roleAllList)

        return "role/form"
    }
}