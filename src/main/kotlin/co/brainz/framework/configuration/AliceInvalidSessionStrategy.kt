package co.brainz.framework.configuration

import org.springframework.http.MediaType
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.session.InvalidSessionStrategy
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AliceInvalidSessionStrategy : InvalidSessionStrategy {

    private val httpSessionRequestCache: HttpSessionRequestCache = HttpSessionRequestCache()

    override fun onInvalidSessionDetected(request: HttpServletRequest, response: HttpServletResponse) {
        httpSessionRequestCache.saveRequest(request, response)
        val requestURI = getRequestUrl(request)
        if (requestURI != "/" && requestURI != "/login") {
            // val ajaxHeader = request.getHeader("X-Requested-With")
            // if ("XMLHttpRequest" == ajaxHeader) {
            // 보통 위의 경우로 ajax 통신을 구분하는데.. 안되서..
            if (isAjaxRequest(request)) {
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                response.sendError(901, "SESSION_TIMED_OUT")
            } else {
                response.sendRedirect("/sessionInValid")
            }
        } else {
            response.sendRedirect(requestURI)
        }
    }

    /**
     *
     */
    private fun getRequestUrl(request: HttpServletRequest): String {
        val requestURL = request.requestURL
        val queryString = request.queryString
        if (StringUtils.hasText(queryString)) {
             requestURL.append("?").append(queryString)
        }
        return requestURL.toString()
    }

    /**
     * AJAX 요청 확인.
     */
    private fun isAjaxRequest(request: HttpServletRequest?): Boolean {
        return request?.getHeader("Sec-Fetch-Mode") != "navigate"
    }

}