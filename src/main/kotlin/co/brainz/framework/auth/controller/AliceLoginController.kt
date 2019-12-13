package co.brainz.framework.auth.controller

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.constants.AliceConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest


/**
 * 로그인 처리 클래스
 */
@Controller
class AliceLoginController(private val userDetailsService: AliceUserDetailsService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 로그인 페이지로 이동한다.
     *
     * request 에 RSA 값을 추가하여 post handler 에서 RSA 키 값들을 셋팅한다.
     * 이미 로그인한 상태면 리다이렉트한다.
     */
    @GetMapping("/login")
    fun login(request: HttpServletRequest): String {
        logger.debug("=> Request move login.")

        var page = "login"
        val aliceUserEntity: AliceUserEntity?

        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)

        val securityContextObject = request.getSession(false)?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)

        logger.debug(">>> securityContextObject {} <<<", securityContextObject)
        logger.debug(">>> securityContextObject is null? {} <<<", securityContextObject == null)

        if (securityContextObject != null) {
            logger.debug(">>> Aleady login. <<<")

            val securityContext = securityContextObject as SecurityContext
            aliceUserEntity = userDetailsService.loadUserByUsername(securityContext.authentication.principal.toString())
            logger.debug("login info {}", aliceUserEntity)
            request.removeAttribute(AliceConstants.RsaKey.USE_RSA.value)
            page = "redirect:"
        }

        return page
    }
}