package co.brainz.itsm.certification.controller

import co.brainz.framework.auth.security.AliceUserDto
import co.brainz.itsm.certification.SignUpDto
import co.brainz.itsm.certification.serivce.CertificationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("certification")
class CertificationRestController {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var certificationService: CertificationService

    //임시 사용자 등록 (가정)
    @PostMapping("/regist")
    fun register(request: HttpServletRequest, signUpDto: SignUpDto) {

        //TODO: 사용자 등록
        var isSuccess: Boolean = false
        isSuccess = true
        if (isSuccess) {
            certificationService.sendMail(signUpDto.userId, signUpDto.email)
        }
    }

    @GetMapping("/sendCertifiedMail")
    fun send() {
        val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        certificationService.sendMail(aliceUserDto.userId, aliceUserDto.email)
    }

}