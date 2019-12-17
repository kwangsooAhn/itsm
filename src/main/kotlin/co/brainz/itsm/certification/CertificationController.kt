package co.brainz.itsm.certification

import co.brainz.framework.constants.AliceConstants
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/certification")
class CertificationController {

    /**
     * 회원 가입 화면을 호출한다.
     * */
    @GetMapping("/signup")
    fun getSignUp(request: HttpServletRequest): String {
        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)
        return "certification/signup"
    }
}