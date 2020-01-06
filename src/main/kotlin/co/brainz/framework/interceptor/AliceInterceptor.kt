package co.brainz.framework.interceptor

import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceUserDto
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.CryptoRsa
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
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
        
        // URL 접근 확인
        urlAccessAuthCheck(request)
        
        return true
    }
    
    /**
     * URL 접근 권한 확인.
     */
    private fun urlAccessAuthCheck(request: HttpServletRequest) {
        
        val securityContextObject = request.getSession(false)?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
        val requestUrl = request.requestURI
        val requestMethod = request.method.toLowerCase()
        logger.debug(">>> Url [{}] {} <<<", requestMethod, requestUrl)

        if (securityContextObject != null && requestUrl != "" && !urlExcludePatternCheck(requestUrl)) {
            val securityContext = securityContextObject as SecurityContext
            val aliceUserDto = securityContext.authentication.details as AliceUserDto
            val regex = "\\{([a-zA-Z]*)}".toRegex()
            val requestUrls = requestUrl.split("/")
            aliceUserDto.urls.let letDto@ {
                urlList ->
                urlList.forEach forEachList@ {
                    val urls = it.url.split("/")
                    if (requestUrls.size == urls.size && requestMethod == it.method) {
                        requestUrls.forEachIndexed {
                            idx, url ->
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
                throw AliceException(AliceErrorConstants.ERR_00003, AliceErrorConstants.ERR_00003.detail)
            }
        }
    }
    
    /**
     * URL 제외 패턴 확인.
     */
    fun urlExcludePatternCheck(requestUrl: String): Boolean {
        val result = AliceConstants.AccessAllowUrlPatten.getAccessAllowUrlPatten().find {
            if ("\\*\\*$".toRegex().containsMatchIn(it)) {
                requestUrl.startsWith(it.replace("**", ""))
            } else {
                requestUrl.contentEquals(it)
            }
        }.isNullOrBlank()
        return !result
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