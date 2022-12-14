/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.token.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.itsm.token.service.TokenService
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/tokens")
class TokenRestController(private val tokenService: TokenService) {

    /**
     * 처리할 문서 데이터 조회.
     * */
    @GetMapping("/{tokenId}/data")
    fun getToken(@PathVariable tokenId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(tokenService.findToken(tokenId))
    }

    /**
     * Post Token 처리.
     */
    @PostMapping("/data")
    fun postToken(
        @RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(tokenService.postToken(restTemplateTokenDataUpdateDto))
    }

    /**
     * Put Token 처리.
     */
    @PutMapping("/{tokenId}/data")
    fun putToken(
        @RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto,
        @PathVariable tokenId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(tokenService.putToken(tokenId, restTemplateTokenDataUpdateDto))
    }

    @GetMapping("/todoCount")
    fun getTodoCount(): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(tokenService.getTodoTokenCount())
    }

    /**
     * 문서함 Excel 다운로드
     */
    @GetMapping("/excel")
    fun getTokensExcelDownload(tokenSearchCondition: TokenSearchCondition): ResponseEntity<ByteArray> {
        return tokenService.getTokensExcelDownload(tokenSearchCondition)
    }

    /**
     * 프로세스 상태 화면.
     */
    @GetMapping("/{instanceId}/status")
    fun getProcessStatus(@PathVariable instanceId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(tokenService.getTokenStatus(instanceId))
    }
}
