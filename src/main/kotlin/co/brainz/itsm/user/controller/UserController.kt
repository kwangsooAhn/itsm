package co.brainz.itsm.user.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.service.UserService
import co.brainz.itsm.user.dto.UserSearchDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * 사용자 관리 클래스
 */
@Controller
@RequestMapping("/users")
class UserController(
        private val codeService: CodeService,
        private val userService: UserService,
        private val roleService: RoleService
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 사용자 검색, 목록 등 메인이 되는 조회 화면을 호출한다.
     */
    @GetMapping("/search")
    fun getUserSearch(model: Model): String {
        model.addAttribute("userSearchCode", codeService.selectCodeByParent(UserConstants.PCODE.value))
        return "user/user"
    }

    /**
     * 사용자 조회 목록 화면을 호출한다.
     */
    @GetMapping("/list")
    fun getUserList(userSearchDto: UserSearchDto, model: Model): String {
        val users = userService.selectUserList(userSearchDto)
        model.addAttribute("users", users)
        return "user/userList"
    }

    /**
     * 사용자 상세 조회 화면을 호출한다.
     */
    @GetMapping("/{userId}/edit")
    fun getUserDetail(@PathVariable userId: String, model: Model): String {
        val users = userService.selectUser(userId)
        val roles = roleService.getRoles(users.roleEntities)
        model.addAttribute("users", users)
        model.addAttribute("roles", roles)

        return "user/userDetail"
    }
}
