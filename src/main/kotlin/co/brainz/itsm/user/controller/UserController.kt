package co.brainz.itsm.user.controller

import co.brainz.framework.constants.AliceConstants
import co.brainz.itsm.code.service.CodeService
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
                     private val userService: UserService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val userPage: String = "user/user"
    private val userListPage: String = "user/userList"
    private val userEditSelfPage: String = "user/userEditSelf"
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
     * 사용자 자기정보 수정화면을 호출한다.
     */
    @GetMapping("/{userKey}/{target}", "/{userKey}/{target}")
    fun getUserEdit(@PathVariable userKey: String, @PathVariable target: String, request: HttpServletRequest, model: Model): String {
        var returnUrl = ""
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

        when (target) {
            "userEditSelf"-> {
                returnUrl = userEditSelfPage
            }
            "userEdit" -> {
                returnUrl =  userEditPage
            }
        }

        return returnUrl
    }

    /**
     * 사용자 등록 화면을 호출한다.
     */
    @GetMapping("/new")
    fun getUserRegister(model: Model): String {
        val langList = codeService.selectCodeByParent(UserConstants.PLANGCODE.value)
        val dateList = codeService.selectCodeByParent(UserConstants.PDATECODE.value)
        val timeList = codeService.selectCodeByParent(UserConstants.PTIMECODE.value)
        val timezoneList = userService.selectTimezoneList()

        model.addAttribute("langList", langList)
        model.addAttribute("timezoneList", timezoneList)
        model.addAttribute("dateList", dateList)
        model.addAttribute("timeList", timeList)
        return userEditPage
    }
}