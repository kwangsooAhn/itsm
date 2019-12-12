package co.brainz.itsm.certification.controller

import co.brainz.framework.util.EncryptionUtil
import co.brainz.itsm.certification.CertificationDto
import co.brainz.itsm.certification.CertificationEnum
import co.brainz.itsm.certification.serivce.CertificationService
import co.brainz.itsm.settings.user.UserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@RequestMapping("/certification")
@Controller
class CertificationController {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var certificationService: CertificationService

    //사용자등록 화면 (임시)
    @GetMapping("/signup")
    fun signupForm(): String {
        return "certification/signup"
    }

    @GetMapping("/status")
    fun getFaqForm(request: HttpServletRequest, model: Model): String {

        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: UserEntity =  certificationService.findByUserId(userId)
        var validCode: Int = CertificationEnum.SIGNUP.value
        if (userDto.status == CertificationEnum.CERTIFIED.code) {
            validCode = CertificationEnum.CERTIFIED.value
        }
        model.addAttribute("validCode", validCode)

        return "certification/status"
    }

    @GetMapping("/valid")
    fun valid(request: HttpServletRequest, @RequestParam(value="uid") uid: String, model: Model): String {
        val decryptUid: String = EncryptionUtil().twoWayDeCode(uid)
        val values: List<String> = decryptUid.split(":".toRegex())
        val userDto: UserEntity =  certificationService.findByUserId(values[1])
        var validCode: Int = CertificationEnum.SIGNUP.value

        when (userDto.status) {
            CertificationEnum.SIGNUP.code -> {
                validCode = when (values[0]) {
                    userDto.certificationCode -> {
                        val certificationDto: CertificationDto = CertificationDto(userDto.userId, userDto.email, "", CertificationEnum.CERTIFIED.code)
                        certificationService.updateUser(certificationDto)
                        CertificationEnum.CERTIFIED.value
                    }
                    else -> {
                        CertificationEnum.ERROR.value
                    }
                }
            }
            CertificationEnum.CERTIFIED.code -> validCode = CertificationEnum.OVER.value
        }
        model.addAttribute("validCode", validCode)

        return "certification/status"
    }
}