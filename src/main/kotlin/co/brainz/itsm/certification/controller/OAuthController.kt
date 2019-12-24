package co.brainz.itsm.certification.controller

import co.brainz.itsm.certification.OAuthDto
import co.brainz.itsm.certification.serivce.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/oauth")
class OAuthController(private val oAuthService: OAuthService,
                      private val oAuthServiceGoogle: OAuthServiceGoogle,
                      private val oAuthServiceFacebook: OAuthServiceFacebook) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @RequestMapping("/{service}/login")
    fun oauthLogin(request: HttpServletRequest, response: HttpServletResponse, @PathVariable service: String) {

        var serviceUrl: String = "redirect:/"
        when (service) {
            "google" -> {
                serviceUrl = oAuthServiceGoogle.serviceUrl()
            }
            "facebook" -> {
                //TODO: facebook
                serviceUrl = oAuthServiceFacebook.serviceUrl()
            }
        }
        response.sendRedirect(serviceUrl)
    }

    @GetMapping("/{service}/callback")
    fun callback(request: HttpServletRequest, @RequestParam(value="code") code: String, @PathVariable service: String, model: Model): String {

        var oAuthDto = OAuthDto()
        when (service) {
            "google" -> {
                val parameters: MultiValueMap<String, String> = oAuthServiceGoogle.setParameters(code)
                oAuthDto = oAuthServiceGoogle.callback(parameters, service)
            }
            "facebook" -> {
                //TODO: facebook
            }
        }
        oAuthService.callbackUrl(oAuthDto)
        return "redirect:/"
    }
}