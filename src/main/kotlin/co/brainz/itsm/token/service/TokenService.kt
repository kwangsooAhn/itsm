/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.token.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.document.service.DocumentActionService
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateCIComponentDataDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class TokenService(
    private val restTemplate: RestTemplateProvider,
    private val documentActionService: DocumentActionService
) {

    /**
     * Post Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun postToken(restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateTokenDataUpdateDto.assigneeId = aliceUserDto.userKey
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.POST_TOKEN.url)
        val responseEntity = restTemplate.create(url, restTemplateTokenDataUpdateDto)

        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * Put Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun putToken(tokenId: String, restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.PUT_TOKEN.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateTokenDataUpdateDto.assigneeId = aliceUserDto.userKey
        val responseEntity = restTemplate.update(url, restTemplateTokenDataUpdateDto)

        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @param restTemplateTokenSearchListDto
     * @return List<tokenDto>
     */
    fun getTokenList(
        restTemplateTokenSearchListDto: RestTemplateTokenSearchListDto
    ): List<RestTemplateInstanceViewDto> {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params.add("userKey", aliceUserDto.userKey)
        params.add("tokenType", restTemplateTokenSearchListDto.searchTokenType)
        params.add("documentId", restTemplateTokenSearchListDto.searchDocumentId)
        params.add("searchValue", restTemplateTokenSearchListDto.searchValue)
        params.add("offset", restTemplateTokenSearchListDto.offset)
        params.add("fromDt", restTemplateTokenSearchListDto.searchFromDt)
        params.add("toDt", restTemplateTokenSearchListDto.searchToDt)
        params.add("tags", restTemplateTokenSearchListDto.searchTags)

        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_INSTANCES.url, parameters = params)
        val responseBody = restTemplate.get(url)

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateInstanceViewDto::class.java)
        )
    }

    /**
     * [tokenId]를 받아서 처리할 문서 상세 조회 하여 [String]반환 한다.
     */
    fun findToken(tokenId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN_DATA.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )

        return documentActionService.makeTokenAction(restTemplate.get(url))
    }

    /**
     * CI 컴포넌트 -  CI 세부 데이터 저장.
     */
    fun saveCIComponentData(ciId: String, ciComponentData: String): Boolean {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val map = mapper.readValue(ciComponentData, LinkedHashMap::class.java)
        val ciComponentDataDto = RestTemplateCIComponentDataDto(
            ciId = map["ciId"] as String,
            componentId = map["componentId"] as String,
            values = mapper.writeValueAsString(map["values"]),
            instanceId = map["instanceId"] as String
        )
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.PUT_CI_COMPONENT.url.replace(
                restTemplate.getKeyRegex(),
                ciId
            )
        )
        val responseEntity = restTemplate.create(url, ciComponentDataDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * CI 컴포넌트 - CI 세부 데이터 삭제.
     */
    fun deleteCIComponentData(params: LinkedMultiValueMap<String, String>): Boolean {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.DELETE_CI_COMPONENT.url,
            parameters = params
        )
        return restTemplate.delete(url).toString().isNotEmpty()
    }
}
