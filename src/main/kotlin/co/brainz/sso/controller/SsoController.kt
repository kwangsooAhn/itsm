/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.sso.controller

import javax.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * SSO 연동
 */
@Controller
@RequestMapping("/itsm")
class SsoController {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val ssoLoginPage = "jsp/sso/KSign/login"

    @Value("\${sso.enabled}")
    private val ssoEnabled: Boolean = false

    @GetMapping("/","")
    fun ssoCheck(response: HttpServletResponse) {
        logger.info("/itsm controller start.")
        return if (ssoEnabled) {
            logger.info("sso 설정 = true")
            response.sendRedirect("/itsm/ssoLogin")
        } else {
            logger.info("sso 설정 = false")
            response.sendRedirect("/portals/main")
        }
    }

    @GetMapping("/ssoLogin")
    fun ssoLogin(response: HttpServletResponse): String {
        logger.info("/itsm/ssoLogin controller start.")
        return ssoLoginPage
    }
}
