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

    companion object {
        const val BADCREDENTIALS_EXCEPTION_HANDLING_NUM = 1
        const val USERNAMENOTFOUND_EXCEPTION_HANDLING_NUM = 2
        const val DISABLED_EXCEPTION_HANDLING_NUM = 3
        const val OTHER_EXCEPTION_HANDLING_NUM = 99
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AuthenticationException
    ) {
        logger.error(e.message)
        val errorCode = when (e) {
            // "가입하지 않은 아이디이거나, 잘못된 비밀번호입니다."
            is BadCredentialsException -> BADCREDENTIALS_EXCEPTION_HANDLING_NUM
            is UsernameNotFoundException -> USERNAMENOTFOUND_EXCEPTION_HANDLING_NUM
            // "계정이 비활성화되었습니다. 관리자에게 문의해주세요"
            is DisabledException -> DISABLED_EXCEPTION_HANDLING_NUM
            // "알 수 없는 에러가 발생하였습니다. 관리자에게 문의해주세요."
            else -> OTHER_EXCEPTION_HANDLING_NUM
        }

        // TODO 로그인 실패 카운트 및 이력 업데이트

        redirectStrategy.sendRedirect(request, response, "/login?authfailed=$errorCode")
    }
}
