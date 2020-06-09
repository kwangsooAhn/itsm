package co.brainz.framework.auth.handler

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component

@Component
class AliceLogoutSuccessHandler : LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        if (authentication != null && authentication.details != null) {
            request.session.invalidate()
        }
        // Set redirectUrl
        if (request.getParameter("redirectUrl") != null) {
            request.session.setAttribute("redirectUrl", request.getParameter("redirectUrl"))
        }
        response.status = HttpServletResponse.SC_OK
        response.sendRedirect("/login")
    }
}
