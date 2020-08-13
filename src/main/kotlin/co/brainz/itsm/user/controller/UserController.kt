package co.brainz.itsm.user.controller

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.avatar.service.AliceAvatarService
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.service.UserService
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * 사용자 관리 클래스
 */
@Controller
@RequestMapping("/users")
class UserController(
    private val codeService: CodeService,
    private val userService: UserService,
    private val roleService: RoleService,
    private val avatarService: AliceAvatarService
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    private val userSearchPage: String = "user/userSearch"
    private val userListPage: String = "user/userList"
    private val userEditSelfPage: String = "user/userEditSelf"
    private val userEditPage: String = "user/userEdit"
    private val departmentPopUpPage: String = "user/departmentPopUp"

    /**
     * 사용자 검색, 목록 등 메인이 되는 조회 화면을 호출한다.
     */
    @GetMapping("/search")
    fun getUserSearch(model: Model): String {
        model.addAttribute("categoryList", codeService.selectCodeByParent(AliceUserConstants.PLATFORM_CATEGORY_P_CODE))
        return userSearchPage
    }

    /**
     * 사용자 조회 목록 화면을 호출한다.
     */
    @GetMapping("/list")
    fun getUserList(
        @RequestParam(value = "search", defaultValue = "") search: String,
        @RequestParam(value = "category", defaultValue = "") category: String,
        model: Model
    ): String {
        model.addAttribute("userList", userService.selectUserList(search, category))
        return userListPage
    }

    /**
     * 사용자 정보 수정 화면 및 자기정보 수정 화면을 호출한다.
     */
    @GetMapping("/{userKey}/{target}")
    fun getUserEdit(
        @PathVariable userKey: String,
        @PathVariable target: String,
        request: HttpServletRequest,
        model: Model
    ): String {
        var returnUrl = ""
        val userEntity = userService.selectUserKey(userKey)
        val users = userMapper.toUserDto(userEntity)
        users.avatarId = userEntity.avatar.avatarId
        users.avatarPath = avatarService.makeAvatarPath(userEntity.avatar)
        users.avatarValue = userEntity.avatar.avatarValue
        users.avatarSize = Paths.get(userEntity.avatar.uploadedLocation).toFile().length()
        val roleEntities = mutableSetOf<AliceRoleEntity>()
        val timeFormat = users.timeFormat!!.split(' ')
        val usersDate = timeFormat[0]
        val usersTime = if (timeFormat.size == 3) {
            timeFormat[1] + ' ' + timeFormat[2]
        } else {
            timeFormat[1]
        }

        userEntity.userRoleMapEntities.forEach { userRoleMap ->
            roleEntities.add(userRoleMap.role)
        }
        val roles = roleService.getRoles(roleEntities)
        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)

        if (users.department?.isBlank() == true) {
            val deptCodeDetail = codeService.getDetailCodes(users.department!!)
            model.addAttribute("deptCodeDetail", deptCodeDetail)
        }

        model.addAttribute("users", users)
        model.addAttribute("roles", roles)
        model.addAttribute("usersDate", usersDate)
        model.addAttribute("usersTime", usersTime)
        model.addAttribute("themeList", codeService.selectCodeByParent(UserConstants.PTHEMECODE.value))
        model.addAttribute("langList", codeService.selectCodeByParent(UserConstants.PLANGCODE.value))
        model.addAttribute("timezoneList", userService.selectTimezoneList())
        model.addAttribute("dateList", codeService.selectCodeByParent(UserConstants.PDATECODE.value))
        model.addAttribute("timeList", codeService.selectCodeByParent(UserConstants.PTIMECODE.value))

        when (target) {
            "editSelf" -> {
                returnUrl = userEditSelfPage
            }
            "edit" -> {
                returnUrl = userEditPage
            }
        }

        return returnUrl
    }

    /**
     * 사용자 등록 화면을 호출한다.
     */
    @GetMapping("/new")
    fun getUserRegister(model: Model): String {
        model.addAttribute("themeList", codeService.selectCodeByParent(UserConstants.PTHEMECODE.value))
        model.addAttribute("langList", codeService.selectCodeByParent(UserConstants.PLANGCODE.value))
        model.addAttribute("timezoneList", userService.selectTimezoneList())
        model.addAttribute("dateList", codeService.selectCodeByParent(UserConstants.PDATECODE.value))
        model.addAttribute("timeList", codeService.selectCodeByParent(UserConstants.PTIMECODE.value))
        model.addAttribute("roles", roleService.getRoles(mutableSetOf()))

        return userEditPage
    }

    /**
     * 부서 관리 팝업 호출
     */
    @GetMapping("/department/view-pop")
    fun getDepartmentPopUp(model: Model): String {
        val departmentList = codeService.selectCodeByParent(UserConstants.PDEPTCODE.value)
        model.addAttribute("departmentList", departmentList)

        return departmentPopUpPage
    }
}
