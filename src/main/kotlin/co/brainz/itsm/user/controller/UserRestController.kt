/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.user.dto.UserSelectListDto
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.service.UserService
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    private val localeResolver: LocaleResolver,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val currentSessionUser: CurrentSessionUser
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    /**
     * 사용자를 등록한다.
     */
    @PostMapping("/", "")
    fun createUser(@RequestBody @Valid aliceSignUpDto: AliceSignUpDto): String {
        val password = userService.makePassword()
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
    fun updateUserEdit(@RequestBody @Valid user: UserUpdateDto): String {
        return userService.updateUserEdit(user, AliceUserConstants.UserEditType.ADMIN_USER_EDIT.code)
    }

    /**
     * 사용자가 자신의 정보를 업데이트한다.
     */
    @PutMapping("/{userKey}/info")
    fun updateUserEditSelf(
        @RequestBody @Valid user: UserUpdateDto,
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
            if (user.userKey == currentSessionUser.getUserKey()) {
                SecurityContextHolder.getContext().authentication =
                    userDetailsService.createNewAuthentication(user.userKey)
            }
        }
        return result
    }

    /**
     * 전체 사용자 목록 조회.
     */
    @GetMapping("/all")
    fun getUsers(): MutableList<UserSelectListDto> {
        return userService.selectUserListOrderByName()
    }

    /**
     * 사용자의 비밀번호를 초기화한다.
     */
    @PutMapping("/{userKey}/resetpassword")
    private fun resetPassword(@PathVariable userKey: String): String {
        val password = userService.makePassword()
        return userService.resetPassword(userKey, password)
    }
}
