package co.brainz.itsm.role.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.ui.Model
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.role.entity.RoleEntity

@Controller
public class RoleController {

	companion object {
		private val logger = LoggerFactory.getLogger(RoleController::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "roleController")
	}

	@Autowired
	lateinit var roleService: RoleService

	@GetMapping("roles/form")
	public fun list(request: HttpServletRequest, model: Model): String {

		var roleList = roleService.getRoleList()
		var authList = roleService.getAuthList()
		var userList = roleService.getUserList()

		if (roleList.size > 0) {
			var roleId = roleList[0].roleId
			var roleDetail = roleService.getRoleDetail(roleId)
			var roleAuthMapList = roleService.getRoleAuthMapList(roleId)
			var userRoleMapList = roleService.getUserRoleMapList(roleId)

			model.addAttribute("roleAuthMapList", roleAuthMapList)
			model.addAttribute("userRoleMapList", userRoleMapList)
			model.addAttribute("roleDetail", roleDetail)
		}

		model.addAttribute("roleList", roleList)
		model.addAttribute("authList", authList)
		model.addAttribute("userList", userList)

		return "roles/form"
	}

}