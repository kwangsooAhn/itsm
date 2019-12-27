package co.brainz.itsm.certification.controller

import co.brainz.framework.auth.entity.AliceUserDto
import co.brainz.itsm.certification.SignUpDto
import co.brainz.itsm.certification.SignUpStatus
import co.brainz.itsm.certification.service.CertificationService
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
        if (result == SignUpStatus.STATUS_SUCCESS.code) {
            certificationService.sendMail(signUpDto.userId, signUpDto.email)
        }
        return result
    }

    @GetMapping("/sendCertifiedMail")
    fun sendCertifiedMail() {
        val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        certificationService.sendMail(aliceUserDto.userId, aliceUserDto.email)
    }

}
