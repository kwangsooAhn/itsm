package co.brainz.framework.configuration

import co.brainz.framework.util.AliceUtil
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.session.InvalidSessionStrategy
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class AliceInvalidSessionStrategy : InvalidSessionStrategy {

    private val httpSessionRequestCache: HttpSessionRequestCache = HttpSessionRequestCache()

    override fun onInvalidSessionDetected(request: HttpServletRequest, response: HttpServletResponse) {
        httpSessionRequestCache.saveRequest(request, response)
        if (!AliceUtil().urlExcludePatternCheck(request)) {
            if ("XMLHttpRequest" == request.getHeader("X-Requested-With")) {
                response.status = HttpServletResponse.SC_FORBIDDEN
            } else {
                response.sendRedirect(request.contextPath + "/sessionInValid")
            }
        } else {
            val requestURL = request.requestURL
            val queryString = request.queryString
            if (StringUtils.hasText(queryString)) {
                requestURL.append("?").append(queryString)
            }
            response.sendRedirect(requestURL.toString())
        }
    }
}
