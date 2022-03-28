/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.apiToken.controller

import co.brainz.api.apiToken.service.ApiTokenService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tokens")
class ApiTokenController(
    private val apiTokenService: ApiTokenService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Access Token 발행
     */
    @GetMapping("")
    fun createAccessToken(
        request: HttpServletRequest
    ): ResponseEntity<*> {
        return apiTokenService.createAccessToken(request)
    }

    /**
     * Refresh Token 으로 Access Token 재발행
     */
    @GetMapping("/refresh")
    fun createAccessTokenByRefreshToken(
        request: HttpServletRequest
    ): ResponseEntity<*> {
        return apiTokenService.createAccessTokenByRefreshToken(request)
    }
}
