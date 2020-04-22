package co.brainz.framework.auth.handler

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

/**
 * 로그인 요청 인증 실패 처리 클래스
 *
 * 인증 실패한 경우 에러메시지와 함께 login 페이지로 포워딩한다.
 */
@Component
class AliceAuthFailureHandler : AuthenticationFailureHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AuthenticationException
    ) {
        logger.error(e.message)
        val errorCode = when (e) {
            // "아이디나 비밀번호가 맞지 않습니다. 다시 확인해주세요."
            is BadCredentialsException -> 1
            // "계정이 비활성화되었습니다. 관리자에게 문의해주세요"
            is DisabledException -> 2
            // "등록되지 않은 계정입니다. 다시 확인해주세요."
            is UsernameNotFoundException -> 3
            // "알 수 없는 에러가 발생하였습니다. 관리자에게 문의해주세요."
            else -> 99
        }

        // TODO 로그인 실패 카운트 및 이력 업데이트

        redirectStrategy.sendRedirect(request, response, "/login?authfailed=$errorCode")
    }
}
