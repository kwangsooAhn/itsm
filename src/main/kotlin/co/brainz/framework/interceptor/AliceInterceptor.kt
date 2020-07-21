package co.brainz.framework.interceptor

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceUtil
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

/**
 * 인터셉터 구현 클래스
 */
@Component
class AliceInterceptor(private val aliceCryptoRsa: AliceCryptoRsa) : HandlerInterceptorAdapter() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        userAgentCheck(request, response)
        // URL 접근 확인
        urlAccessAuthCheck(request)
        return true
    }

    /**
     * User Agent 체크, IE 브라우저 접근 제한
     */
    private fun userAgentCheck(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        val userAgent = request.getHeader("User-Agent")

        return if (userAgent != null && (userAgent.indexOf("MISE") != -1 || userAgent.indexOf("Trident") != -1)) {
            response.sendRedirect("/portal/browserGuide")
            false
        } else {
            true
        }
    }

    /**
     * URL 접근 권한 확인.
     */
    private fun urlAccessAuthCheck(request: HttpServletRequest) {
        val securityContextObject =
            request.getSession(false)?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
        val requestUrl = request.requestURI
        val requestMethod = request.method.toLowerCase()

        if (securityContextObject != null && requestUrl != "" && !AliceUtil().urlExcludePatternCheck(request)) {
            val securityContext = securityContextObject as SecurityContext
            val aliceUserDto = securityContext.authentication.details as AliceUserDto
            val regex = "\\{([a-zA-Z]*)}".toRegex()
            val requestUrls = requestUrl.split("/")
            aliceUserDto.urls.let letDto@{ urlList ->
                urlList?.forEach forEachList@{
                    val urls = it.url.split("/")
                    if (requestUrls.size == urls.size && requestMethod == it.method) {
                        requestUrls.forEachIndexed { idx, url ->
                            if (urls[idx] == url || regex.containsMatchIn(urls[idx])) {
                                if (idx == requestUrls.size - 1) {
                                    return@letDto
                                }
                            } else {
                                return@forEachList
                            }
                        }
                    }
                }
                throw AliceException(
                    AliceErrorConstants.ERR_00003,
                    AliceErrorConstants.ERR_00003.message + "(URL: $requestUrl)"
                )
            }
        }
    }

    @Throws(Exception::class)
    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        val useRsa = request.getAttribute(AliceConstants.RsaKey.USE_RSA.value)
        if (useRsa != null) {
            logger.debug(">>> create RSA key <<< {}", request.requestURL)
            aliceCryptoRsa.resetKeyPair()
            val session = request.getSession(true)
            session.removeAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value)
            session.setAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value, aliceCryptoRsa.getPrivateKey())
            request.setAttribute(AliceConstants.RsaKey.PUBLIC_MODULE.value, aliceCryptoRsa.getPublicKeyModulus())
            request.setAttribute(AliceConstants.RsaKey.PUBLIC_EXPONENT.value, aliceCryptoRsa.getPublicKeyExponent())
        }

        var requestInfo = request.method + "|URI:" + request.requestURI
        if (modelAndView != null) {
            requestInfo += "|Viewname: " + modelAndView.viewName
        }
        logger.debug("Request Info|{}", requestInfo)
    }
}
