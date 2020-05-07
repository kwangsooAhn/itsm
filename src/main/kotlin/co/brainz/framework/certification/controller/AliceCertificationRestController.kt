package co.brainz.framework.certification.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.constants.AliceUserConstants
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("certification")
class AliceCertificationRestController(private val aliceCertificationService: AliceCertificationService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/register")
    fun setUser(@RequestBody aliceSignUpDto: AliceSignUpDto): String {
        val result = aliceCertificationService.createUser(aliceSignUpDto, AliceUserConstants.USER_ID)
        if (result == AliceUserConstants.SignUpStatus.STATUS_SUCCESS.code) {
            aliceCertificationService.sendMail(
                aliceSignUpDto.userId,
                aliceSignUpDto.email,
                AliceUserConstants.SendMailStatus.CREATE_USER.code,
                null
            )
        }
        return result
    }

    @GetMapping("/sendCertifiedMail")
    fun sendCertifiedMail() {
        val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        aliceCertificationService.sendMail(
            aliceUserDto.userId,
            aliceUserDto.email,
            AliceUserConstants.SendMailStatus.CREATE_USER.code,
            null
        )
    }
}
