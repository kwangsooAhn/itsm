package co.brainz.framework.interceptor

import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceUserDto
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.CryptoRsa
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 인터셉터 구현 클래스
 */
@Component
class AliceInterceptor(private val cryptoRsa: CryptoRsa): HandlerInterceptorAdapter() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val securityContextObject = request.getSession(false)?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
        val requestUrl = request.requestURI
        val requestMethod = request.method.toLowerCase()
        logger.debug(">>> Url [{}] {} <<<", requestMethod, requestUrl)

        if (securityContextObject != null && requestUrl != "") {
            val securityContext = securityContextObject as SecurityContext
            val aliceUserDto = securityContext.authentication.details as AliceUserDto
            val regex = "\\{([0-9a-zA-Z]*)}".toRegex()
            aliceUserDto.urls.let {
                urlList ->
                if (urlList.contains(AliceUrlEntity(requestUrl, requestMethod))) {
                    return true
                } else {
                    urlList.forEach forEachDto@ {
                        val requestUrls = requestUrl.split("/")
                        val urls = it.url.split("/")
                        if (regex.containsMatchIn(it.url) && requestMethod == it.method && requestUrls.size == urls.size) {
                            requestUrls.forEachIndexed {
                                idx, url ->
                                if (urls[idx] == url || regex.containsMatchIn(urls[idx])) {
                                    if (idx == requestUrls.size - 1) {
                                        return true
                                    }
                                } else {
                                    return@forEachDto
                                }
                            }
                        }
                    }
                }
            }
            response.sendRedirect("/layout/authFailed")
            return false
        }
        return true
    }

    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {

        val useRsa = request.getAttribute(AliceConstants.RsaKey.USE_RSA.value)
        if (useRsa != null) {
            logger.debug(">>> create RSA key <<< {}", request.requestURL)
            cryptoRsa.resetKeyPair()
            val session = request.getSession(true)
            session.removeAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value)
            session.setAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value, cryptoRsa.getPrivateKey())
            request.setAttribute(AliceConstants.RsaKey.PUBLIC_MODULE.value, cryptoRsa.getPublicKeyModulus())
            request.setAttribute(AliceConstants.RsaKey.PUBLIC_EXPONENT.value, cryptoRsa.getPublicKeyExponent())
        }


//        if (modelAndView != null) {
//
//        }

    }


}