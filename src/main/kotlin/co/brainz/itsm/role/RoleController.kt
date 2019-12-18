package co.brainz.itsm.role

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMethod

import org.springframework.ui.Model
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import javax.servlet.http.HttpServletResponse

import co.brainz.itsm.role.RoleEntity
import co.brainz.itsm.auth.AuthEntity
import co.brainz.itsm.role.RoleService

data class RoleParamClass(
    var roleId: String? = null, var roleName: String? = null, var roleDesc: String? = null,
    var createUserid: String? = null, var arrAuthId: Array<String>? = null,
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

    var roleService: RoleService

    constructor(roleService: RoleService) {
        this.roleService = roleService
    }

    @RequestMapping(path = ["/form"], method = [RequestMethod.GET])
    public fun getRolelist(request: HttpServletRequest, model: Model): String {

        var roleAllList = roleService.getRoleList()
        var authAllList = roleService.getAuthList()

        if (roleAllList.size > 0) {
            var roleId = roleAllList[0].roleId
            var roleDetail = roleService.getRoleDetail(roleId)
            var roleAuthMapList = roleAllList[0].authEntityList
            var authList = mutableListOf<AuthEntity>()
            //아래의 부분은 Entity에서 구현해야 하지 않을까? 라는 고민이 있음.
            if (roleAuthMapList != null) {
                var i = 0
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
                model.addAttribute("authList", authList)
            }
            model.addAttribute("roleDetail", roleDetail)
        }

        model.addAttribute("roleList", roleAllList)

        return "role/form"
    }

    //역할 세부 조회
    @RequestMapping(path = ["/{id}"], method = [RequestMethod.GET])
    public fun getRoleFrom(@PathVariable id: String, model: Model): String {
        var roleAllList = roleService.getRoleList()
        var authAllList = roleService.getAuthList()
        var roleId = id
        if (roleId != "") {
            var roleDetail = roleService.getRoleDetail(roleId)
            var roleAuthMapList = roleDetail[0].authEntityList
            var authList = mutableListOf<AuthEntity>()
            if (roleAuthMapList != null) {
                var i = 0
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
                model.addAttribute("authList", authList)
            }
            model.addAttribute("roleDetail", roleDetail)
        }

        model.addAttribute("roleList", roleAllList)
        return "role/form"
    }

    //역할 저장
    @RequestMapping(path = ["/insertRole"], method = [RequestMethod.POST, RequestMethod.PUT])
    @ResponseBody
    fun insertRole(@RequestBody roleInfo: RoleParamClass, model: Model): String {
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

        var inputDate = LocalDateTime.now()
        var result = roleService.insertRole(
            RoleEntity(
                roleId = roleInfo.roleId.toString(),
                roleName = roleInfo.roleName.toString(),
                roleDesc = roleInfo.roleDesc.toString(),
                createUserid = roleInfo.createUserid.toString(),
                createDt = inputDate,
                updateUserid = roleInfo.createUserid.toString(),
                updateDt = inputDate,
                authEntityList = authEntityList
            )
        )

        return result.roleId
    }

    //역할 삭제
    @RequestMapping(path = ["/{roleId}"], method = [RequestMethod.DELETE])
    @ResponseBody
    fun deleteRole(@PathVariable roleId: String): String {
        var result = "false"
        try {
            if (roleId != null) {
                roleService.deleteRole(roleId)
                result = "true"
            } else {
                result = "false"
            }
        } catch (e: Exception) {
            result = "false"
        }

        return result
    }
}