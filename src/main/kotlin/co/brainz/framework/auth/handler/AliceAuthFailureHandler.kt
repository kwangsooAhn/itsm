package co.brainz.framework.auth.handler

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 로그인 요청 인증 실패 처리 클래스
 *
 * 인증 실패한 경우 에러메시지와 함께 login 페이지로 포워딩한다.
 */
@Component
class AliceAuthFailureHandler : AuthenticationFailureHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {
        logger.error(e.message)
        val errorMsg = when (e) {
            is BadCredentialsException -> "아이디나 비밀번호가 맞지 않습니다. 다시 확인해주세요."
            is DisabledException -> "계정이 비활성화되었습니다. 관리자에게 문의하세요."
            is CredentialsExpiredException -> "비밀번호 유효기간이 만료 되었습니다. 관리자에게 문의하세요."
            is UsernameNotFoundException -> "아이디나 비밀번호가 맞지 않습니다. 다시 확인해주세요."
            else -> "기타 에러 발생. 에러코드 확인후 추가 요망.\n" + e.message
        }


        // TODO 로그인 실패 카운트 및 이력 업데이트

        //에러메시지 + 로그인 아이디 값 전달
        request.setAttribute("errorMsg", errorMsg)
        request.setAttribute("uid", request.getParameter("uid"))
        request.getRequestDispatcher("/login").forward(request, response)
    }
}