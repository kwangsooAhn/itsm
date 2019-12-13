package co.brainz.itsm.certification.controller


import co.brainz.itsm.certification.CertificationEnum
import co.brainz.itsm.certification.serivce.CertificationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
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
    fun status(request: HttpServletRequest, model: Model): String {
        val validCode: Int = certificationService.status()
        model.addAttribute("validCode", validCode)
        return "certification/status"
    }

    @GetMapping("/valid")
    fun valid(request: HttpServletRequest, @RequestParam(value="uid", defaultValue = "") uid: String, model: Model): String {
        var validCode: Int = CertificationEnum.ERROR.value
        if (uid != "") {
            validCode = certificationService.valid(uid)
        }
        model.addAttribute("validCode", validCode)
        return "certification/status"
    }
}