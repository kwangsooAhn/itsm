/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.sso.controller

import co.brainz.framework.constants.AliceConstants
import co.brainz.sso.service.SsoLoginService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * SSO 연동
 */
@Controller
@RequestMapping("/itsm")
class SsoController(
    private val ssoLoginService: SsoLoginService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val ssoLoginPage = "jsp/sso/KSign/login"
    private val ssoTokenCheckPage = "/itsm/sso"
    private val portalMainPage = "/portals/main"

    @Value("\${sso.enabled}")
    private val ssoEnabled: Boolean = false

    /**
     * SSO 설정 여부 확인.
     */
    @GetMapping("/","")
    fun ssoCheck(response: HttpServletResponse) {
        return if (ssoEnabled) {
            response.sendRedirect(ssoTokenCheckPage)
        } else {
            response.sendRedirect(portalMainPage)
        }
    }

    /**
     * SSO 토큰 발급 여부 확인하는 화면으로 이동. (login.jsp)
     */
    @GetMapping("/sso")
    fun ssoTokenCheck(request: HttpServletRequest): String {
        request.setAttribute(AliceConstants.RsaKey.USE_RSA.value, AliceConstants.RsaKey.USE_RSA.value)
        return ssoLoginPage
    }

    /**
     * SSO 토큰 발급이 된 후 ITSM 로그인 처리.
     */
    @PostMapping("/ssoLogin")
    fun ssoLogin(request: HttpServletRequest, response: HttpServletResponse) {
        ssoLoginService.ssoLogin(request)
        response.sendRedirect("/login")
    }
}
