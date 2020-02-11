package co.brainz.itsm.user.controller

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.constants.AliceConstants
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * 사용자 관리 클래스
 */
@Controller
@RequestMapping("/users")
class UserController(private val codeService: CodeService,
                     private val userService: UserService,
                     private val roleService: RoleService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val userPage: String = "user/user"
    private val userListPage: String = "user/userList"
    private val userDetailPage: String = "user/userDetail"
    private val userEditPage: String = "user/userEdit"

    /**
     * 사용자 검색, 목록 등 메인이 되는 조회 화면을 호출한다.
     */
    @GetMapping("/search")
    fun getUserSearch(model: Model): String {
        return userPage
    }

    /**
     * 사용자 조회 목록 화면을 호출한다.
     */
    @GetMapping("/list")
    fun getUserList(searchValue: String, model: Model): String {
        model.addAttribute("users", userService.selectUserList(searchValue))
        return userListPage
    }

    /**
     * 사용자 상세 조회 화면을 호출한다.
     */
    @GetMapping("/{userId}/edit")
    fun getUserDetail(@PathVariable userId: String, model: Model): String {
        val users = userService.selectUser(userId)
        val roleEntities = mutableSetOf<AliceRoleEntity>()

        users.userRoleMapEntities.forEach {userRoleMap ->
            roleEntities.add(userRoleMap.role)
        }

        val roles = roleService.getRoles(roleEntities)
        model.addAttribute("users", users)
        model.addAttribute("roles", roles)

        return userDetailPage
    }

    /**
     * 사용자 자기정보 수정화면을 호출한다.
     */
    @GetMapping("/{userKey}/userEdit")
    fun getUserEdit(@PathVariable userKey: String, request: HttpServletRequest, model: Model): String {
        val users = userService.selectUserKey(userKey)
        val timeFormat = users.timeFormat.split(' ')
        val usersDate = timeFormat[0].toString()
        val usersTime = if (timeFormat.size == 3) { timeFormat[1] + ' ' + timeFormat[2] } else { timeFormat[1] }

        val langList = codeService.selectCodeByParent(UserConstants.PLANGCODE.value)
        val dateList = codeService.selectCodeByParent(UserConstants.PDATECODE.value)
        val timeList = codeService.selectCodeByParent(UserConstants.PTIMECODE.value)
        val timezoneList = userService.selectTimezoneList()
        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)

        model.addAttribute("users", users)
        model.addAttribute("usersDate", usersDate)
        model.addAttribute("usersTime", usersTime)
        model.addAttribute("langList", langList)
        model.addAttribute("timezoneList", timezoneList)
        model.addAttribute("dateList", dateList)
        model.addAttribute("timeList", timeList)
        return userEditPage
    }
}