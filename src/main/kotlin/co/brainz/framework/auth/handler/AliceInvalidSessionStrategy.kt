package co.brainz.framework.auth.handler

import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.session.InvalidSessionStrategy
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AliceInvalidSessionStrategy: InvalidSessionStrategy {

    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    override fun onInvalidSessionDetected(request: HttpServletRequest, response: HttpServletResponse) {
        val requestUrl = getRequestUrl(request)
        val ajaxHeader = request.getHeader("X-Requested-With")
        if ("XMLHttpRequest" == ajaxHeader) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Ajax Request Denied (Session Expired)")
        } else {
            redirectStrategy.sendRedirect(request, response, "/sessionInValid")
        }
    }

    /**
     * request url.
     */
    private fun getRequestUrl(request: HttpServletRequest): String? {
        val requestURL = request.requestURL
        val queryString = request.queryString
        if (StringUtils.hasText(queryString)) {
            requestURL.append("?").append(queryString)
        }
        return requestURL.toString()
    }

}