package co.brainz.itsm.certification.controller

import co.brainz.itsm.certification.OAuthDto
import co.brainz.itsm.certification.constants.OAuthConstants
import co.brainz.itsm.certification.service.OAuthService
import co.brainz.itsm.certification.service.OAuthServiceGoogle
import co.brainz.itsm.certification.service.OAuthServiceKakao
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
                      private val oAuthServiceKakao: OAuthServiceKakao) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val redirectPage: String = "redirect:/"

    @RequestMapping("/{platform}/login")
    fun oAuthLogin(request: HttpServletRequest, response: HttpServletResponse, @PathVariable platform: String) {

        var platformUrl: String = "redirect:/"
        when (platform) {
            OAuthConstants.PlatformEnum.GOOGLE.value -> {
                platformUrl = oAuthServiceGoogle.platformUrl()
            }
            OAuthConstants.PlatformEnum.KAKAO.value -> {
                platformUrl = oAuthServiceKakao.platformUrl()
            }

        }
        response.sendRedirect(platformUrl)
    }

    @GetMapping("/{platform}/callback")
    fun callback(request: HttpServletRequest, @RequestParam(value="code") code: String, @PathVariable platform: String, model: Model): String {

        var oAuthDto = OAuthDto()
        when (platform) {
            OAuthConstants.PlatformEnum.GOOGLE.value -> {
                val parameters: MultiValueMap<String, String> = oAuthServiceGoogle.setParameters(code)
                oAuthDto = oAuthServiceGoogle.callback(parameters, OAuthConstants.PlatformEnum.GOOGLE.code)
            }
            OAuthConstants.PlatformEnum.KAKAO.value -> {
                val error = request.getParameter("error")
                when (request.getParameter("error")) {
                    null -> {
                        val parameters: MultiValueMap<String, String> = oAuthServiceKakao.setParameters(code)
                        oAuthDto = oAuthServiceKakao.callback(parameters, OAuthConstants.PlatformEnum.KAKAO.code)
                    }
                    else -> logger.error(error)
                }
            }
        }
        when (oAuthDto.userid.isNotEmpty()) {
            true -> oAuthService.callbackUrl(oAuthDto)
            false -> logger.error("oAuth account is not exists.")
        }
        return redirectPage
    }
}
