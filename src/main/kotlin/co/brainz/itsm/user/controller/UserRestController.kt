package co.brainz.itsm.user.controller

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.UserConstants
import co.brainz.framework.certification.service.CertificationService
import co.brainz.itsm.user.service.UserService
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.dto.UserSearchDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.LocaleResolver
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import co.brainz.framework.auth.dto.AliceUserDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import co.brainz.framework.auth.service.AliceAuthProvider

/**
 * 사용자 관리 데이터 처리 클래스
 */
@RestController
@RequestMapping("/rest/users")
class UserRestController(
    private val certificationService: CertificationService,
    private val userService: UserService,
    private val localeResolver: LocaleResolver,
    private val aliceAuthProvider: AliceAuthProvider
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 사용자 전체 목록을 조회한다.
     */
    @GetMapping("/", "")
    fun getUsers(userSearchDto: UserSearchDto): MutableList<AliceUserEntity> {
        return userService.selectUserList(userSearchDto)
    }

    /**
     * 사용자 ID로 해당 정보를 1건 조회한다.
     */
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): AliceUserEntity {
        return userService.selectUser(userId)
    }

    /**
     * 사용자를 업데이트한다.
     */
    @PutMapping("/{userId}")
    fun updateUser(user: UserUpdateDto): AliceUserEntity {
        return userService.updateUser(user)
    }

    /**
     * 사용자가 자신의 정보를 업데이트한다.
     */
    @PutMapping("/{userKey}/userEdit")
    fun updateUserEdit(
        @RequestBody user: UserUpdateDto, request: HttpServletRequest,
        response: HttpServletResponse
    ): String {
        val result = userService.updateUserEdit(user)

        if (result == UserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_EMAIL.code) {
            certificationService.sendMail(
                user.userId,
                user.email!!,
                UserConstants.SendMailStatus.UPDATE_USER_EMAIL.code
            )
        } else {
            certificationService.sendMail(user.userId, user.email!!, UserConstants.SendMailStatus.UPDATE_USER.code)
        }
        localeResolver.setLocale(request, response, Locale(user.lang))
        if (SecurityContextHolder.getContext().authentication != null) {
            val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
            if (user.userKey.equals(aliceUserDto.userKey)) {
                SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(user));
            }
        }
        return result
    }

     /**
     * 변경된 사용자 정보를 SecurityContextHolder에 update한다.
     */
    fun createNewAuthentication(User: UserUpdateDto): Authentication {
        val aliceUser = userService.selectUserKey(User.userKey)
        val authorities = aliceAuthProvider.authorities(aliceUser)
        val authList = aliceAuthProvider.authList(aliceUser)
        val menuList = aliceAuthProvider.menuList(authList)
        val urlList = aliceAuthProvider.urlList(authList)
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(aliceUser.userId, aliceUser.password, authorities)
        usernamePasswordAuthenticationToken.details = AliceUserDto(
            aliceUser.userKey, aliceUser.userId, aliceUser.userName, aliceUser.email,
            aliceUser.useYn, aliceUser.tryLoginCount, aliceUser.expiredDt, aliceUser.oauthKey,
            authorities, menuList, urlList, aliceUser.timezone, aliceUser.lang, aliceUser.timeFormat
        )
        return usernamePasswordAuthenticationToken
    }
}
