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
import co.brainz.itsm.user.entity.UserEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMethod
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDateTime

data class UserParamClass(var userid: String? = null, var username: String? = null)
data class RoleParamClass(
	var roleId: String? = null, var roleName: String? = null, var roleDesc: String? = null,
	var createUserId: String? = null, var arrAuthId: Array<String>? = null,
	var arrUserId: Array<String>? = null
)

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

			var userRoleMapList = roleList[0].userEntityList
			var roleAuthMapList = roleList[0].authEntityList
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

			model.addAttribute("userList", userRoleMapList)
			model.addAttribute("authList", authList)
			model.addAttribute("roleDetail", roleDetail)
		}

		model.addAttribute("roleList", roleList)

		return "role/form"
	}

	//역할 세부 조회
	@GetMapping("/{id}")
	public fun getRoleFrom(@PathVariable id: String, model: Model): String {
		var roleList = roleService.getRoleList()
		var authAllList = roleService.getAuthList()
		var roleId = id
		if (roleId != "") {
			var roleDetail = roleService.getRoleDetail(roleId)
			var userRoleMapList = roleDetail[0].userEntityList
			var roleAuthMapList = roleDetail[0].authEntityList
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

			model.addAttribute("userList", userRoleMapList)
			model.addAttribute("authList", authList)
			model.addAttribute("roleDetail", roleDetail)
		}

		model.addAttribute("roleList", roleList)
		return "role/form"
	}

	//사용자 조회
	@PostMapping("/getUserId")
	@ResponseBody
	fun getUserId(@RequestBody userId: UserParamClass, model: Model): UserEntity {
		logger.debug(">>> mapperd {} <<<", userId)
		var userid = userId.userid.toString()

		if (roleService.getUserId(userid).isNotEmpty()) {
			return roleService.getUserId(userid).get(0)
		} else {
			return UserEntity(
				userId = "", userName = "", password = "",
				email = "", useYn = false, tryLoginCount = 0, createId = "", updateId = ""
			)
		}
	}

	//역할 저장
	@PostMapping("/insertRole")
	@ResponseBody
	fun insertRole(@RequestBody roleInfo: RoleParamClass, model: Model): String {
		logger.debug(">>> mapperd {} <<<", roleInfo)

		var userEntityList: List<UserEntity> = mutableListOf<UserEntity>()
		var authEntityList: List<AuthEntity> = mutableListOf<AuthEntity>()

		var roleAuthMapList = mutableListOf<AuthEntity>()
		if (roleInfo.arrAuthId != null) {
			var authList = roleInfo.arrAuthId
			for (auth in authList!!.indices) {
				roleAuthMapList.add(
					AuthEntity(
						authId = authList[auth]
					)
				)
			}
			authEntityList = roleAuthMapList
		}

		var userRoleMapList = mutableListOf<UserEntity>()
		if (roleInfo.arrUserId != null) {
			var userList = roleInfo.arrUserId
			for (user in userList!!.indices) {
				userRoleMapList.add(
					UserEntity(
						userId = userList[user]
					)
				)
			}
			userEntityList = userRoleMapList
		}

		var inputDate = LocalDateTime.now()
		roleService.insertRole(
			RoleEntity(
				roleId = roleInfo.roleId.toString(),
				roleName = roleInfo.roleName.toString(),
				roleDesc = roleInfo.roleDesc.toString(),
				createId = roleInfo.createUserId.toString(),
				createDate = inputDate,
				updateId = roleInfo.createUserId.toString(),
				updateDate = inputDate,
				userEntityList = userEntityList,
				authEntityList = authEntityList
			)
		)

		return "role/form"
	}

	//역할 삭제
	@PostMapping("/deleteRole")
	@ResponseBody
	fun deleteRole(@RequestBody roleInfo: RoleParamClass, model: Model): String {
		logger.debug(">>> mapperd {} <<<", roleInfo)

		if (roleInfo.roleId != null) {
			roleService.deleteRole(roleInfo.roleId.toString())
		}

		return "role/form"
	}
}