package co.brainz.framework.certification.controller

import co.brainz.framework.certification.dto.AliceOAuthDto
import co.brainz.framework.certification.service.AliceOAuthServiceGoogle
import co.brainz.framework.certification.service.AliceOAuthServiceKakao
import co.brainz.framework.certification.service.OAuthService
import co.brainz.framework.constants.AliceUserConstants
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
class AliceOAuthController(private val oAuthService: OAuthService,
                           private val oAuthServiceGoogle: AliceOAuthServiceGoogle,
                           private val oAuthServiceKakao: AliceOAuthServiceKakao) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val redirectPage: String = "redirect:/"

    @RequestMapping("/{platform}/login")
    fun oAuthLogin(request: HttpServletRequest, response: HttpServletResponse, @PathVariable platform: String) {

        var platformUrl: String = "redirect:/"
        when (platform) {
            AliceUserConstants.Platform.GOOGLE.value -> {
                platformUrl = oAuthServiceGoogle.platformUrl()
            }
            AliceUserConstants.Platform.KAKAO.value -> {
                platformUrl = oAuthServiceKakao.platformUrl()
            }

        }
        response.sendRedirect(platformUrl)
    }

    @GetMapping("/{platform}/callback")
    fun callback(request: HttpServletRequest, @RequestParam(value="code") code: String, @PathVariable platform: String, model: Model): String {

        var oAuthDto = AliceOAuthDto()
        when (platform) {
            AliceUserConstants.Platform.GOOGLE.value -> {
                val parameters: MultiValueMap<String, String> = oAuthServiceGoogle.setParameters(code)
                oAuthDto = oAuthServiceGoogle.callback(parameters, AliceUserConstants.Platform.GOOGLE.code)
            }
            AliceUserConstants.Platform.KAKAO.value -> {
                val error = request.getParameter("error")
                when (request.getParameter("error")) {
                    null -> {
                        val parameters: MultiValueMap<String, String> = oAuthServiceKakao.setParameters(code)
                        oAuthDto = oAuthServiceKakao.callback(parameters, AliceUserConstants.Platform.KAKAO.code)
                    }
                    else -> logger.error(error)
                }
            }
        }
        when (oAuthDto.oauthKey.isNotEmpty()) {
            true -> oAuthService.callbackUrl(oAuthDto)
            false -> logger.error("oAuth account is not exists.")
        }
        return redirectPage
    }
}
