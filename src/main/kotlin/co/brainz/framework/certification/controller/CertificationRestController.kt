package co.brainz.framework.certification.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.certification.dto.SignUpDto
import co.brainz.framework.certification.service.CertificationService
import co.brainz.framework.constants.UserConstants
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("certification")
class CertificationRestController(private val certificationService: CertificationService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/register")
    fun setUser(@RequestBody signUpDto: SignUpDto): String {
        val result = certificationService.insertUser(signUpDto)
        if (result == UserConstants.SignUpStatus.STATUS_SUCCESS.code) {
            certificationService.sendMail(signUpDto.userId, signUpDto.email, UserConstants.SendMailStatus.CREATE_USER.code)
        }
        return result
    }

    @GetMapping("/sendCertifiedMail")
    fun sendCertifiedMail() {
        val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        certificationService.sendMail(aliceUserDto.userId, aliceUserDto.email, UserConstants.SendMailStatus.CREATE_USER.code)
    }

}
