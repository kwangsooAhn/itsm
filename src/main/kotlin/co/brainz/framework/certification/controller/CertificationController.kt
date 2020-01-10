package co.brainz.framework.certification.controller


import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.certification.service.CertificationService
import co.brainz.framework.constants.UserConstants
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/certification")
class CertificationController(private val certificationService: CertificationService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/signup")
    fun getSignUp(request: HttpServletRequest): String {
        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)
        return "certification/signup"
    }

    @GetMapping("/status")
    fun status(request: HttpServletRequest, model: Model): String {
        val validCode: Int = certificationService.status()
        model.addAttribute("validCode", validCode)
        return "certification/status"
    }

    @GetMapping("/valid")
    fun valid(request: HttpServletRequest, @RequestParam(value="uid", defaultValue = "") uid: String, model: Model): String {
        var validCode: Int = UserConstants.Status.ERROR.value
        if (uid != "") {
            validCode = certificationService.valid(uid)
        }
        model.addAttribute("validCode", validCode)
        return "certification/status"
    }
}
