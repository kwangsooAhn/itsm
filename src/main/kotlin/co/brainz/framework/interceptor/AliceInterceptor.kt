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
        val securityContextObject = request.getSession(false)?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
        var requestUrl = request.requestURI
        val requestMethod = request.method.toLowerCase()
        logger.debug(">>> Url [{}] {} <<<", requestMethod, requestUrl)

        if (securityContextObject != null && requestUrl != "" && !urlExcludePatternCheck(requestUrl)) {
            val securityContext = securityContextObject as SecurityContext
            val aliceUserDto = securityContext.authentication.details as AliceUserDto
            val regex = "\\{([a-zA-Z]*)}".toRegex()
            val requestUrls = requestUrl.split("/")
            // 방법 1. 사용자 url과 requestUrl 패턴이 동일한 경우 requestUrl에 값을 넣은 후 확인하는 방법.
            /*
            aliceUserDto.urls.let {
                urlList ->
                urlList.forEach forEachDto@ {
                    val urls = it.url.split("/")
                    if (requestUrls.size == urls.size && requestMethod == it.method) {
                        val id = "{id}"
                        val url = it.url
                        val idx = url.indexOf(id)
                        if (idx > -1 && requestUrl.startsWith(url.substring(0, idx))
                                && requestUrl.endsWith(url.substring(idx + id.length))) {
                            requestUrl = url
                        }
                    }
                }
                if (urlList.contains(AliceUrlEntity(requestUrl, requestMethod))) {
                    return true
                } else {
                    throw AliceException(AliceErrorConstants.ERR_00003, AliceErrorConstants.ERR_00003.detail)
                }
            }
            */
            // 방법 2. url을 /로 잘라 비교하는 방법. 이때 {값}은 정규식으로 확인한 후 그냥 넘어간다.
            aliceUserDto.urls.let {
                urlList ->
                urlList.forEach forEachDto@ {
                    val urls = it.url.split("/")
                    if (requestUrls.size == urls.size && requestMethod == it.method) {
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
            throw AliceException(AliceErrorConstants.ERR_00003, AliceErrorConstants.ERR_00003.detail)
        }
        return true
    }

    fun urlExcludePatternCheck(requestUrl: String): Boolean {
        val result = AliceInterceptorConstants.getExcludePattens().find {
            if ("\\*\\*$".toRegex().containsMatchIn(it)) {
                requestUrl.startsWith(it.replace("**", ""))
            } else {
                requestUrl.contentEquals(it)
            }
        }
        result?.let { return true }
        return false
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