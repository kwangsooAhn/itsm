/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.certification.controller

import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.user.constants.UserConstants
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/certification")
class AliceCertificationRestController(
    private val aliceCertificationService: AliceCertificationService,
    private val aliceCertificationMailService: AliceCertificationMailService,
    private val currentSessionUser: CurrentSessionUser
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("")
    fun setUser(@RequestBody aliceSignUpDto: AliceSignUpDto): ResponseEntity<ZResponse> {
        val result = aliceCertificationService.createUser(aliceSignUpDto, UserConstants.USER_ID)
        if (result.status == ZResponseConstants.STATUS.SUCCESS.code) {
            aliceCertificationMailService.sendMail(
                aliceSignUpDto.userId,
                aliceSignUpDto.email,
                UserConstants.SendMailStatus.CREATE_USER.code,
                null
            )
        }
        return ZAliceResponse.response(result)
    }

    @GetMapping("/certifiedmail")
    fun sendCertifiedMail() {
        aliceCertificationMailService.sendMail(
            currentSessionUser.getUserId(),
            currentSessionUser.getEmail(),
            UserConstants.SendMailStatus.CREATE_USER.code,
            null
        )
    }
}
