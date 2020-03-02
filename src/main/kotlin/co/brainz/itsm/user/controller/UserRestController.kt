package co.brainz.itsm.user.controller

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.certification.dto.SignUpDto
import co.brainz.framework.constants.UserConstants
import co.brainz.framework.certification.service.CertificationService
import co.brainz.framework.encryption.CryptoRsa
import co.brainz.itsm.user.service.UserService
import co.brainz.itsm.user.dto.UserUpdateDto
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.LocaleResolver
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.random.Random

/**
 * 사용자 관리 데이터 처리 클래스
 */
@RestController
@RequestMapping("/rest/users")
class UserRestController(
    private val certificationService: CertificationService,
    private val userService: UserService,
    private val userDetailsService: AliceUserDetailsService,
    private val localeResolver: LocaleResolver,
    private val cryptoRsa: CryptoRsa
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)


    /**
     * 사용자를 등록한다.
     */
    @PostMapping("/", "")
    fun createUser(@RequestBody signUpDto: SignUpDto): String {
        val fromNum = 1000000000
        val toNum = 9999999999
        val random = Random
        val randomNumber = random.nextLong(toNum - fromNum) + fromNum
        val password = randomNumber.toString()
        val publicKey = cryptoRsa.getPublicKey()

        // 패스워드 암호화
        signUpDto.password = cryptoRsa.encrypt(publicKey, password)

        val result = certificationService.createUser(signUpDto, UserConstants.ADMIN_ID)
        if (result == UserConstants.SignUpStatus.STATUS_SUCCESS.code) {
            certificationService.sendMail(signUpDto.userId, signUpDto.email, UserConstants.SendMailStatus.CREATE_USER_ADMIN.code, password)
        }
        return result
    }

    /**
     * 사용자가 정보를 업데이트한다.
     */
    @PutMapping("/{userKey}/userEditSelf", "/{userKey}/userEdit")
    fun updateUserEdit(
        @RequestBody user: UserUpdateDto, request: HttpServletRequest,
        response: HttpServletResponse
    ): String {
        val result = userService.updateUserEdit(user)

        if (result == UserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_EMAIL.code) {
            certificationService.sendMail(
                user.userId,
                user.email!!,
                UserConstants.SendMailStatus.UPDATE_USER_EMAIL.code,
                null
            )
        } else {
            certificationService.sendMail(user.userId, user.email!!, UserConstants.SendMailStatus.UPDATE_USER.code, null)
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
        var aliceUser: AliceUserAuthDto = userMapper.toAliceUserAuthDto(userService.selectUserKey(User.userKey))
        aliceUser = userDetailsService.getAuthInfo(aliceUser)

        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(aliceUser.userId, aliceUser.password, aliceUser.grantedAuthorises)
        usernamePasswordAuthenticationToken.details = aliceUser.grantedAuthorises?.let { grantedAuthorises ->
            aliceUser.urls?.let { urls ->
                aliceUser.menus?.let { menus ->
                    AliceUserDto(
                            aliceUser.userKey, aliceUser.userId, aliceUser.userName, aliceUser.email, aliceUser.useYn,
                            aliceUser.tryLoginCount, aliceUser.expiredDt, aliceUser.oauthKey, grantedAuthorises,
                            menus, urls, aliceUser.timezone, aliceUser.lang, aliceUser.timeFormat
                    )
                }
            }
        }
        return usernamePasswordAuthenticationToken
    }
}
