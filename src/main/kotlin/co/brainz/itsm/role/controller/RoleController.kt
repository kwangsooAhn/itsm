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
import co.brainz.itsm.role.entity.AuthEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable

@RequestMapping("/roles")
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

	@GetMapping("/form")
	public fun getRolelist(request: HttpServletRequest, model: Model): String {

		var roleList = roleService.getRoleList()
		var authAllList = roleService.getAuthList()

		if (roleList.size > 0) {
			var roleId = roleList[0].roleId
			var roleDetail = roleService.getRoleDetail(roleId)

			var userRoleMapList = roleService.getUserRoleMapList(roleId)
			var userAllList = roleService.getUserList()
			val userList = mutableListOf<AliceUserEntity>()
			//서로다른 Entity에서 조회 하도록 해봄
			for (user in userAllList) {
				for (userRoleMap in userRoleMapList) {
					if (user.userId == userRoleMap.userId) {
						userList.add(user)
					}
				}
			}

			var roleAuthMapList = roleService.getAuthRoleMapList(roleId)
			var authList = mutableListOf<AuthEntity>()
			var i = 0;
			for (auth in authAllList) {
				for (roleAuthMap in roleAuthMapList) {
					if (auth.authId == roleAuthMap.authId) {
						i = 1
						break
					} else {
						i = 0
					}
				}

				if (i == 1) {
					auth.authDesc = "checked"
					authList.add(auth)
					i = 0
				} else {
					authList.add(auth)
				}
			}

			model.addAttribute("userList", userList)
			model.addAttribute("authList", authList)
			model.addAttribute("roleDetail", roleDetail)
		}

		model.addAttribute("roleList", roleList)

		return "role/form"
	}

	//역할 세부 조회
	@GetMapping("/{id}")
	public fun getRoleDetails(@PathVariable id: String, model: Model): String {

		var roleList = roleService.getRoleList()
		var authAllList = roleService.getAuthList()
		var roleId = id
		if (roleId != "") {
			var roleDetail = roleService.getRoleDetail(roleId)

			var userRoleMapList = roleService.getUserRoleMapList(roleId)
			var userAllList = roleService.getUserList()
			val userList = mutableListOf<AliceUserEntity>()
			//서로다른 Entity에서 조회 하도록 해봄
			for (user in userAllList) {
				for (userRoleMap in userRoleMapList) {
					if (user.userId == userRoleMap.userId) {
						userList.add(user)
					}
				}
			}

			var roleAuthMapList = roleService.getAuthRoleMapList(roleId)
			var authList = mutableListOf<AuthEntity>()
			var i = 0;
			for (auth in authAllList) {
				for (roleAuthMap in roleAuthMapList) {
					if (auth.authId == roleAuthMap.authId) {
						i = 1
						break
					} else {
						i = 0
					}
				}

				if (i == 1) {
					auth.authDesc = "checked"
					authList.add(auth)
					i = 0
				} else {
					authList.add(auth)
				}
			}

			model.addAttribute("userList", userList)
			model.addAttribute("authList", authList)
			model.addAttribute("roleDetail", roleDetail)
		}

		model.addAttribute("roleList", roleList)

		return "role/form"
	}


}