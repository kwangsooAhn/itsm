/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.apiToken.controller

import co.brainz.api.apiToken.service.ApiTokenService
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
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
        request: HttpServletRequest,
        @RequestBody bodyContent: String
    ): ResponseEntity<*> {
        return apiTokenService.createAccessToken(bodyContent)
    }

    /**
     * Refresh Token 으로 Access Token 재발행
     */
    @GetMapping("/refresh")
    fun createAccessTokenByRefreshToken(
        request: HttpServletRequest,
        @RequestBody bodyContent: String
    ): ResponseEntity<*> {
        return apiTokenService.createAccessTokenByRefreshToken(bodyContent)
    }
}
