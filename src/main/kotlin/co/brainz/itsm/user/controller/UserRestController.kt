/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.controller

import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.user.dto.UserCustomDto
import co.brainz.itsm.user.dto.UserSearchCondition
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.dto.UserUpdatePasswordDto
import co.brainz.itsm.user.service.UserService
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
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
    private val currentSessionUser: CurrentSessionUser,
    private val roleService: RoleService
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${spring.mail.enabled}")
    private val mailEnabled: Boolean = false

    /**
     * 사용자를 등록한다.
     */
    @PostMapping("/", "")
    fun createUser(@RequestBody @Valid aliceSignUpDto: AliceSignUpDto): ResponseEntity<ZResponse> {
        val password = when {
            mailEnabled -> userService.makePassword()
            else -> aliceSignUpDto.password
        }
        val publicKey = aliceCryptoRsa.getPublicKey()
        aliceSignUpDto.password = password?.let { aliceCryptoRsa.encrypt(publicKey, it) }

        val result = aliceCertificationService.createUser(aliceSignUpDto, AliceUserConstants.ADMIN_ID)
        if (result.status == ZResponseConstants.STATUS.SUCCESS.code) {
            aliceCertificationMailService.sendMail(
                aliceSignUpDto.userId,
                aliceSignUpDto.email,
                AliceUserConstants.SendMailStatus.CREATE_USER_ADMIN.code,
                password
            )
        }
        return ZAliceResponse.response(result)
    }

    /**
     * 사용자가 다른 사용자의 정보를 업데이트한다.
     */
    @PutMapping("/{userKey}/all")
    fun updateUserEdit(
        @RequestBody @Valid user: UserUpdateDto,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<ZResponse> {
        val result = userService.updateUserEdit(user, AliceUserConstants.UserEditType.ADMIN_USER_EDIT.code)
        if (result.status == ZResponseConstants.STATUS.SUCCESS.code ||
            result.status == ZResponseConstants.STATUS.SUCCESS_EDIT.code ||
            result.status == ZResponseConstants.STATUS.SUCCESS_EDIT_EMAIL.code ||
            result.status == ZResponseConstants.STATUS.SUCCESS_EDIT_PASSWORD.code
        ) {
            if (SecurityContextHolder.getContext().authentication != null) {
                if (user.userKey == currentSessionUser.getUserKey()) {
                    localeResolver.setLocale(request, response, Locale(user.lang))
                    SecurityContextHolder.getContext().authentication =
                        userDetailsService.createNewAuthentication(user.userKey)
                }
            }
        }
        return ZAliceResponse.response(result)
    }

    /**
     * 사용자가 자신의 정보를 업데이트한다.
     */
    @PutMapping("/{userKey}/info")
    fun updateUserEditSelf(
        @RequestBody @Valid user: UserUpdateDto,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<ZResponse> {
        var result: ZResponse? = null
        val userSessionRoleCheck = userService.userSessionRoleCheck(user.userKey, setOf(AliceConstants.SYSTEM_ROLE))
        if (userSessionRoleCheck == ZResponseConstants.STATUS.SUCCESS.code) {
            result = userService.updateUserEdit(user, AliceUserConstants.UserEditType.SELF_USER_EDIT.code)
            if (result.status == ZResponseConstants.STATUS.SUCCESS.code ||
                result.status == ZResponseConstants.STATUS.SUCCESS_EDIT.code ||
                result.status == ZResponseConstants.STATUS.SUCCESS_EDIT_EMAIL.code ||
                result.status == ZResponseConstants.STATUS.SUCCESS_EDIT_PASSWORD.code
            ) {
                localeResolver.setLocale(request, response, Locale(user.lang))
                if (SecurityContextHolder.getContext().authentication != null) {
                    if (user.userKey == currentSessionUser.getUserKey()) {
                        SecurityContextHolder.getContext().authentication =
                            userDetailsService.createNewAuthentication(user.userKey)
                    }
                }
            }
        }
        return when (userSessionRoleCheck) {
            ZResponseConstants.STATUS.SUCCESS.code -> ZAliceResponse.response(ZResponse(status = result!!.status))
            else -> ZAliceResponse.response(ZResponse(status = userSessionRoleCheck))
        }
    }

    /**
     * 사용자의 처리할 문서를 위임자로 변경
     */
    @PostMapping("/absence")
    fun executeUserProcessingDocumentAbsence(
        @RequestBody absenceInfo: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            userService.executeUserProcessingDocumentAbsence(absenceInfo)
        )
    }

    /**
     * 전체 사용자 목록 조회.
     */
    @GetMapping("/all")
    fun getUsers(): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(userService.selectUserListOrderByName())
    }

    /**
     * 사용자의 비밀번호를 초기화한다.
     */
    @PutMapping("/{userKey}/resetpassword")
    private fun resetPassword(@PathVariable userKey: String): ResponseEntity<ZResponse> {
        val password = userService.makePassword()
        return ZAliceResponse.response(userService.resetPassword(userKey, password))
    }

    /**
     * 사용자 정의 색상 조회.
     */
    @GetMapping("colors")
    fun getUserCustomColors(): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(userService.getUserCustomColors())
    }

    /**
     * 사용자 정의 색상 추가.
     */
    @PutMapping("colors")
    private fun updateUserCustomColors(
        @RequestBody userCustomDto: UserCustomDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(userService.updateUserCustomColors(userCustomDto))
    }

    @PutMapping("/updatePassword")
    private fun updatePassword(
        @RequestBody userUpdatePasswordDto: UserUpdatePasswordDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(userService.updatePassword(userUpdatePasswordDto))
    }

    @PutMapping("/nextTime")
    private fun extendExpiryDate(
        @RequestBody userUpdatePasswordDto: UserUpdatePasswordDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(userService.extendExpiryDate(userUpdatePasswordDto))
    }

    /**
     * 사용자 목록 Excel 다운로드
     */
    @GetMapping("/excel")
    fun getUsersExcelDownload(
        userSearchCondition: UserSearchCondition
    ): ResponseEntity<ByteArray> {
        return userService.getUsersExcelDownload(userSearchCondition)
    }

    /**
     * 사용자 비밀번호 확인 시 rsa key 전달
     */
    @GetMapping("/rsa")
    fun rsaKeySend(): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(userService.rsaKeySend())
    }

    /**
     * 사용자 비밀번호 확인
     */
    @PostMapping("/passwordConfirm")
    fun userPasswordConfirm(
        @RequestBody data: HashMap<String, Any>
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(userService.userPasswordConfirm(data))
    }
}
