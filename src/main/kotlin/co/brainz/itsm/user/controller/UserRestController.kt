/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.controller

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.avatar.service.AliceAvatarService
import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.itsm.user.dto.UserListDto
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.service.UserService
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.random.Random
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.LocaleResolver

/**
 * 사용자 관리 데이터 처리 클래스
 */
@RestController
@RequestMapping("/rest/users")
class UserRestController(
    private val aliceCertificationService: AliceCertificationService,
    private val aliceCertificationMailService: AliceCertificationMailService,
    private val userService: UserService,
    private val userDetailsService: AliceUserDetailsService,
    private val avatarService: AliceAvatarService,
    private val localeResolver: LocaleResolver,
    private val aliceCryptoRsa: AliceCryptoRsa
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    /**
     * 사용자를 등록한다.
     */
    @PostMapping("/", "")
    fun createUser(@RequestBody aliceSignUpDto: AliceSignUpDto): String {
        val fromNum = 1000000000
        val toNum = 9999999999
        val randomNumber = Random.nextLong(toNum - fromNum) + fromNum
        val password = randomNumber.toString()
        val publicKey = aliceCryptoRsa.getPublicKey()
        aliceSignUpDto.password = aliceCryptoRsa.encrypt(publicKey, password)

        val result = aliceCertificationService.createUser(aliceSignUpDto, AliceUserConstants.ADMIN_ID)
        if (result == AliceUserConstants.SignUpStatus.STATUS_SUCCESS.code) {
            aliceCertificationMailService.sendMail(
                aliceSignUpDto.userId,
                aliceSignUpDto.email,
                AliceUserConstants.SendMailStatus.CREATE_USER_ADMIN.code,
                password
            )
        }
        return result
    }

    /**
     * 사용자가 다른 사용자의 정보를 업데이트한다.
     */
    @PutMapping("/{userKey}/all")
    fun updateUserEdit(@RequestBody user: UserUpdateDto): String {
        return userService.updateUserEdit(user, AliceUserConstants.UserEditType.ADMIN_USER_EDIT.code)
    }

    /**
     * 사용자가 자신의 정보를 업데이트한다.
     */
    @PutMapping("/{userKey}/info")
    fun updateUserEditSelf(
        @RequestBody user: UserUpdateDto,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): String {
        val result = userService.updateUserEdit(user, AliceUserConstants.UserEditType.SELF_USER_EDIT.code)
        when (result) {
            AliceUserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_EMAIL.code -> {
                aliceCertificationMailService.sendMail(
                    user.userId,
                    user.email!!,
                    AliceUserConstants.SendMailStatus.UPDATE_USER_EMAIL.code,
                    null
                )
            }
            else -> aliceCertificationMailService.sendMail(
                user.userId,
                user.email!!,
                AliceUserConstants.SendMailStatus.UPDATE_USER.code,
                null
            )
        }
        localeResolver.setLocale(request, response, Locale(user.lang))
        if (SecurityContextHolder.getContext().authentication != null) {
            val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
            if (user.userKey == aliceUserDto.userKey) {
                SecurityContextHolder.getContext().authentication = createNewAuthentication(user)
            }
        }
        return result
    }

    /**
     * 변경된 사용자 정보를 SecurityContextHolder에 update한다.
     */
    fun createNewAuthentication(user: UserUpdateDto): Authentication {
        var aliceUser: AliceUserAuthDto = userMapper.toAliceUserAuthDto(userService.selectUserKey(user.userKey))
        aliceUser = userDetailsService.getAuthInfo(aliceUser)
        val avatarPath = avatarService.makeAvatarPath(aliceUser.avatar)
        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(aliceUser.userId, aliceUser.password, aliceUser.grantedAuthorises)
        usernamePasswordAuthenticationToken.details = aliceUser.grantedAuthorises?.let { grantedAuthorises ->
            aliceUser.urls?.let { urls ->
                aliceUser.menus?.let { menus ->
                    AliceUserDto(
                        aliceUser.userKey, aliceUser.userId, aliceUser.userName, aliceUser.email, aliceUser.position,
                        aliceUser.department, aliceUser.officeNumber, aliceUser.mobileNumber, aliceUser.useYn,
                        aliceUser.tryLoginCount, aliceUser.expiredDt, aliceUser.oauthKey, grantedAuthorises,
                        menus, urls, aliceUser.timezone, aliceUser.lang, aliceUser.timeFormat, aliceUser.theme, avatarPath
                    )
                }
            }
        }
        return usernamePasswordAuthenticationToken
    }

    /**
     * 전체 사용자 목록 조회.
     */
    @GetMapping("/", "")
    fun getUsers(): MutableList<UserListDto> {
        return userService.selectUserListOrderByName()
    }
}
