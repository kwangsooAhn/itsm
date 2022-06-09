/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.controller

import co.brainz.framework.auth.dto.AliceUserSimpleDto
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AliceLoginRestController(
    private val userDetailsService: AliceUserDetailsService
) {

    /**
     * 중복 로그인인지 체크
     */
    @PostMapping("/rest/login")
    fun checkLoginSession(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestBody userSimpleDto: AliceUserSimpleDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(userDetailsService.duplicateSessionCheck(request, response, userSimpleDto))
    }
}
