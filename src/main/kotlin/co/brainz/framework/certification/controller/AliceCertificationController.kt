/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.certification.controller

import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.constants.AliceConstants
import co.brainz.itsm.user.constants.UserConstants
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/certification")
class AliceCertificationController(private val aliceCertificationService: AliceCertificationService) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val signupPage: String = "certification/signup"
    private val statusPage: String = "certification/status"

    @GetMapping("/signup")
    fun getSignUp(request: HttpServletRequest): String {
        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)
        return signupPage
    }

    @GetMapping("/status")
    fun status(request: HttpServletRequest, model: Model): String {
        val validCode: Int = aliceCertificationService.status()
        model.addAttribute("validCode", validCode)
        return statusPage
    }

    @GetMapping("/valid")
    fun valid(
        request: HttpServletRequest,
        @RequestParam(value = "uid", defaultValue = "") uid: String,
        model: Model
    ): String {
        var validCode: Int = UserConstants.UserStatus.ERROR.value
        if (uid != "") {
            validCode = aliceCertificationService.valid(uid)
        }
        model.addAttribute("validCode", validCode)
        return statusPage
    }
}
