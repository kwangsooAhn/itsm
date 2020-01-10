package co.brainz.framework.certification.controller

import co.brainz.framework.certification.dto.OAuthDto
import co.brainz.framework.certification.service.OAuthService
import co.brainz.framework.certification.service.OAuthServiceGoogle
import co.brainz.framework.certification.service.OAuthServiceKakao
import co.brainz.framework.constants.UserConstants
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

    @RequestMapping("/{platform}/login")
    fun oAuthLogin(request: HttpServletRequest, response: HttpServletResponse, @PathVariable platform: String) {

        var platformUrl: String = "redirect:/"
        when (platform) {
            UserConstants.Platform.GOOGLE.value -> {
                platformUrl = oAuthServiceGoogle.platformUrl()
            }
            UserConstants.Platform.KAKAO.value -> {
                platformUrl = oAuthServiceKakao.platformUrl()
            }

        }
        response.sendRedirect(platformUrl)
    }

    @GetMapping("/{platform}/callback")
    fun callback(request: HttpServletRequest, @RequestParam(value="code") code: String, @PathVariable platform: String, model: Model): String {

        var oAuthDto = OAuthDto()
        when (platform) {
            UserConstants.Platform.GOOGLE.value -> {
                val parameters: MultiValueMap<String, String> = oAuthServiceGoogle.setParameters(code)
                oAuthDto = oAuthServiceGoogle.callback(parameters, UserConstants.Platform.GOOGLE.code)
            }
            UserConstants.Platform.KAKAO.value -> {
                val error = request.getParameter("error")
                when (request.getParameter("error")) {
                    null -> {
                        val parameters: MultiValueMap<String, String> = oAuthServiceKakao.setParameters(code)
                        oAuthDto = oAuthServiceKakao.callback(parameters, UserConstants.Platform.KAKAO.code)
                    }
                    else -> logger.error(error)
                }
            }
        }
        when (oAuthDto.userid.isNotEmpty()) {
            true -> oAuthService.callbackUrl(oAuthDto)
            false -> logger.error("oAuth account is not exists.")
        }
        return "redirect:/"
    }
}
