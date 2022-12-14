/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.controller

import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.organization.service.OrganizationService
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.dto.UserSearchCompCondition
import co.brainz.itsm.user.dto.UserSearchCondition
import co.brainz.itsm.user.service.UserService
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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
class UserController(
    private val codeService: CodeService,
    private val userService: UserService,
    private val roleService: RoleService,
    private val userDetailsService: AliceUserDetailsService,
    private val organizationService: OrganizationService,
    private val aliceMessageSource: AliceMessageSource
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    private val userSearchPage: String = "user/userSearch"
    private val userListPage: String = "user/userList"
    private val userEditSelfPage: String = "user/userEditSelf"
    private val userPage: String = "user/user"
    private val userListModalPage: String = "user/userListModal"

    @Value("\${spring.mail.enabled}")
    private val mailEnabled: Boolean = false

    @Value("\${user.register.enable}")
    private val userRegisterEnabled: Boolean = true

    /**
     * 사용자 검색, 목록 등 메인이 되는 조회 화면을 호출한다.
     */
    @GetMapping("/search")
    fun getUserSearch(model: Model): String {
        model.addAttribute("categoryList", codeService.selectCodeByParent(UserConstants.UserPCode.PLATFORM_CATEGORY_P_CODE.code))
        model.addAttribute("userRegisterEnabled", userRegisterEnabled)
        return userSearchPage
    }

    /**
     * 사용자 조회 목록 화면을 호출한다.
     */
    @GetMapping("")
    fun getUserList(userSearchCondition: UserSearchCondition, model: Model): String {
        val result = userService.selectUserList(userSearchCondition)
        model.addAttribute("userList", result.data)
        model.addAttribute("paging", result.paging)
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
        users.avatarPath = userDetailsService.makeAvatarPath(userEntity)
        users.avatarSize = userService.getUserAvatarSize(userEntity)
        users.notificationChannel = userService.getNotificationChannelInfo(userEntity)
        if (users.absenceYn) {
            users.absence = userService.getUserAbsenceInfo(users.userKey)
        }
        val timeFormat = users.timeFormat!!.split(' ')
        val usersDate = timeFormat[0]
        val usersTime = if (timeFormat.size == 3) {
            timeFormat[1] + ' ' + timeFormat[2]
        } else {
            timeFormat[1]
        }

        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)

        if (!users.department.isNullOrBlank()) {
            val organizationDetail = organizationService.getDetailOrganization(users.department!!)
            model.addAttribute("organizationDetail", organizationDetail)
        }

        val allCodes = userService.getInitCodeList()

        model.addAttribute("users", users)
        model.addAttribute("usersDate", usersDate)
        model.addAttribute("usersTime", usersTime)
        model.addAttribute("timezoneList", userService.selectTimezoneList())
        model.addAttribute("themeList", allCodes["themeList"])
        model.addAttribute("langList", allCodes["langList"])
        model.addAttribute("dateList", allCodes["dateList"])
        model.addAttribute("timeList", allCodes["timeList"])

        when (target) {
            UserConstants.UserEdit.EDIT_SELF.code -> {
                val userSessionRoleCheck = userService.userSessionRoleCheck(userKey, setOf(AliceConstants.SYSTEM_ROLE))
                if (userSessionRoleCheck == ZResponseConstants.STATUS.SUCCESS.code) {
                    returnUrl = userEditSelfPage
                } else {
                    throw AliceException(
                        AliceErrorConstants.ERR_00003,
                        aliceMessageSource.getMessage("auth.msg.accessDenied")
                    )
                }
            }
            UserConstants.UserEdit.EDIT.code -> {
                model.addAttribute("allRoles", roleService.getAllRoleList())
                model.addAttribute("userRoles", roleService.getUserRoleList(userKey))
                model.addAttribute("mailEnabled", mailEnabled)
                model.addAttribute("view", false)
                returnUrl = userPage
            }
            UserConstants.UserEdit.VIEW.code -> {
                model.addAttribute("allRoles", roleService.getAllRoleList())
                model.addAttribute("userRoles", roleService.getUserRoleList(userKey))
                model.addAttribute("mailEnabled", mailEnabled)
                model.addAttribute("view", true)
                returnUrl = userPage
            }
        }

        return returnUrl
    }

    /**
     * 사용자 정보 수정 화면 및 자기정보 수정 화면 대리 근무자 리스트 모달
     */
    @GetMapping("/substituteUsers")
    fun getSubUsersList(request: HttpServletRequest, model: Model): String {
        val params = LinkedHashMap<String, Any>()
        params["search"] = request.getParameter("search")
        params["from"] = request.getParameter("from")
        params["to"] = request.getParameter("to")
        params["userKey"] = request.getParameter("userKey")
        model.addAttribute("userList", userService.selectNotAbsenceUserList(params).data)
        model.addAttribute("multiSelect", request.getParameter("multiSelect"))
        return userListModalPage
    }

    /**
     * 사용자 검색 컴포넌트 리스트 모달
     */
    @GetMapping("/searchUsers")
    fun getSearchUsersList(userCompSearchCondition: UserSearchCompCondition, model: Model): String {
        model.addAttribute("userList", userService.getSearchUserList(userCompSearchCondition).data)
        model.addAttribute("multiSelect", false)
        return userListModalPage
    }

    /**
     * 사용자 등록 화면을 호출한다.
     */
    @GetMapping("/new")
    fun getUserRegister(request: HttpServletRequest, model: Model): String {
        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)

        val allCodes = userService.getInitCodeList()
        model.addAttribute("defaultTimezone", UserConstants.DEFAULT_TIMEZONE)
        model.addAttribute("timezoneList", userService.selectTimezoneList())
        model.addAttribute("allRoles", roleService.getAllRoleList())
        model.addAttribute("mailEnabled", mailEnabled)
        model.addAttribute("themeList", allCodes["themeList"])
        model.addAttribute("langList", allCodes["langList"])
        model.addAttribute("dateList", allCodes["dateList"])
        model.addAttribute("timeList", allCodes["timeList"])
        model.addAttribute("view", false)

        return userPage
    }
}
