package co.brainz.framework.auth.handler

import co.brainz.framework.auth.dto.AliceUserDto
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.LocaleResolver

/**
 * 로그인 요청 인증 성공 처리 클래스
 *
 * 부가적인 정보를 추가적으로 담는다.
 * 로그인 성공시 "/login"으로 리다이렉트한다.
 */
@Component
class AliceAuthSuccessHandler(
    private val localeResolver: LocaleResolver
) : SimpleUrlAuthenticationSuccessHandler() {

    private val thisLogger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        thisLogger.debug(
            "isAuthenticated:{}\nname:{}\nprincipal:{}\nauthorities:{}\ncredentials:{}\ndetails:{}",
            auth.isAuthenticated,
            auth.name,
            auth.principal,
            auth.authorities,
            auth.credentials,
            auth.details
        )

        val aliceUserDto = auth.details as AliceUserDto
        localeResolver.setLocale(request, response, Locale(aliceUserDto.lang))

        // TODO 로그인 실패 카운트 0 으로 초기화 및 이력 업데이트

        //Check redirectUrl
        when (val session = request.session) {
            null -> {
                super.onAuthenticationSuccess(request, response, authentication)
            }
            else -> {
                val redirectUrl = session.getAttribute("redirectUrl")
                if (redirectUrl != null) {
                    session.removeAttribute("redirectUrl")
                    redirectStrategy.sendRedirect(request, response, redirectUrl.toString())
                } else {
                    redirectStrategy.sendRedirect(request, response, "/login")
                }
            }
        }
    }
}
