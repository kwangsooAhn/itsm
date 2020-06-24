package co.brainz.itsm.token.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
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
    private val restTemplate: RestTemplateProvider
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

        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_INSTANCES.url, parameters = params)
        val responseBody = restTemplate.get(url)

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateInstanceViewDto::class.java)
        )
    }

    /**
     * 처리할 문서 상세 조회.
     *
     * @return List<tokenDto>
     */
    fun findToken(tokenId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN_DATA.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )
        return restTemplate.get(url)
    }
}
