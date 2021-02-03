/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.token.controller

import co.brainz.itsm.token.service.TokenService
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenSearchListDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.servlet.http.HttpServletRequest
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.DeleteMapping
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
     * 문서함 목록 조회.
     * */
    @GetMapping("")
    fun getTokenList(
        restTemplateTokenSearchListDto: RestTemplateTokenSearchListDto
    ): List<RestTemplateInstanceViewDto> {
        return tokenService.getTokenList(restTemplateTokenSearchListDto)
    }

    /**
     * 처리할 문서 데이터 조회.
     * */
    @GetMapping("/{tokenId}/data")
    fun getToken(@PathVariable tokenId: String): String {
        return tokenService.findToken(tokenId)
    }

    /**
     * Post Token 처리.
     */
    @PostMapping("/data")
    fun postToken(@RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        return tokenService.postToken(restTemplateTokenDataUpdateDto)
    }

    /**
     * Put Token 처리.
     */
    @PutMapping("/{tokenId}/data")
    fun putToken(
        @RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto,
        @PathVariable tokenId: String
    ): Boolean {
        return tokenService.putToken(tokenId, restTemplateTokenDataUpdateDto)
    }

    /**
     * CI 컴포넌트 - CI 세부 정보 등록
     */
    @PostMapping("/cis/{ciId}/data")
    fun saveCIComponentData(@PathVariable ciId: String, @RequestBody ciComponentData: String): Boolean {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val map = mapper.readValue(ciComponentData, LinkedHashMap::class.java)

        return tokenService.saveCIComponentData(ciId, ciComponentData)
    }

    /**
     * CI 컴포넌트 - CI 세부 정보 삭제
     */
    @DeleteMapping("/cis/data")
    fun deleteCIComponentData(request: HttpServletRequest): Boolean {
        val params = LinkedMultiValueMap<String, String>()
        params["ciId"] = request.getParameter("ciId")
        params["componentId"] = request.getParameter("componentId")
        return tokenService.deleteCIComponentData(params)
    }
}
