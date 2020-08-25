package co.brainz.framework.auth.controller

import co.brainz.framework.auth.dto.AliceIpVerificationDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.service.AliceIpVerificationService
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.constants.AliceConstants
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

/**
 * 로그인 처리 클래스
 */
@Controller
class AliceLoginController(
    private val userDetailsService: AliceUserDetailsService,
    private val aliceIpVerificationService: AliceIpVerificationService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val invalidSessionPage: String = "/sessionInvalid"
    private val layoutPage: String = "/layout"

    @Value("\${ip.access.control}")
    lateinit var ipAccessControlValue: String

    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    /**
     * 로그인 페이지로 이동한다.
     *
     * request 에 RSA 값을 추가하여 post handler 에서 RSA 키 값들을 셋팅한다.
     * 이미 로그인한 상태면 리다이렉트한다.
     */
    @GetMapping("/login")
    fun login(request: HttpServletRequest, model: Model): String {
        logger.debug("=> Request move login.")

        var page = "login"
        val aliceUserEntity: AliceUserEntity?
        var clientIp: String? = request.getHeader("X-Forwarded-For")

        if (ipAccessControlValue == "true") {
            val _ipList = aliceIpVerificationService.getIpList()
            val ipList = mutableListOf<AliceIpVerificationDto>()
            for (_ip in _ipList) {
                ipList.add(userMapper.toIpVerificationDto(_ip))
            }

            // Client의 ip 정보를 확인한다.
            if (clientIp == null) {
                clientIp = request.getHeader("Proxy-Client-IP")
            }
            if (clientIp == null) {
                clientIp = request.getHeader("WL-Proxy-Client-IP")
            }
            if (clientIp == null) {
                clientIp = request.getHeader("HTTP_CLIENT_IP")
            }
            if (clientIp == null) {
                clientIp = request.getHeader("HTTP_X_FORWARDED_FOR")
            }
            if (clientIp == null) {
                clientIp = request.remoteAddr
            }
            logger.info("INFO{} ", clientIp)
            model.addAttribute("ipList", ipList)
            model.addAttribute("clientIp", clientIp)
        }

        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)

        val securityContextObject =
            request.getSession(false)?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)

        logger.debug(">>> securityContextObject {} <<<", securityContextObject)
        logger.debug(">>> securityContextObject is null? {} <<<", securityContextObject == null)

        if (securityContextObject != null) {
            logger.debug(">>> Already login. <<<")

            val securityContext = securityContextObject as SecurityContext
            aliceUserEntity = userDetailsService.loadUserByUsername(securityContext.authentication.principal.toString())
            logger.debug("login info {}", aliceUserEntity)
            request.removeAttribute(AliceConstants.RsaKey.USE_RSA.value)
            page = "redirect:$layoutPage"
        }

        return page
    }

    /**
     * Invalid Session 상태에서 redirect 되는 페이지.
     */
    @GetMapping("/sessionInValid")
    fun sessionExpired(request: HttpServletRequest, response: HttpServletResponse, model: Model): String {
        if (HttpSessionRequestCache().getRequest(request, response) != null) {
            model.addAttribute("redirectUrl", HttpSessionRequestCache().getRequest(request, response).redirectUrl)
        }
        model.addAttribute("counter", AliceConstants.SESSION_INVALID_AUTO_REDIRECT_TIME)
        return invalidSessionPage
    }
}
