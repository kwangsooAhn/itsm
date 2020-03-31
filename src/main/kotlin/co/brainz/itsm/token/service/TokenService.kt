package co.brainz.itsm.token.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.itsm.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class TokenService(private val restTemplate: RestTemplateProvider
                   , private val aliceFileService: AliceFileService) {

    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Token 신규 등록 / 처리
     * isComplete : false일 경우에는 저장, true일 경우에 처리
     *
     * @return Boolean
     */
    fun createToken(restTemplateTokenDto: RestTemplateTokenDto): String {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.POST_TOKEN_DATA.url)
        val result = restTemplate.create(url, restTemplateTokenDto)
        restTemplateTokenDto.fileDataIds?.let { aliceFileService.uploadFiles(it) }
        return result
        val responseEntity = restTemplate.create(url, restTemplateTokenDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                val dataDto = mapper.readValue(responseEntity.body.toString(), RestTemplateFormDto::class.java)
                dataDto.formId
            }
            false -> ""
        }
    }

    /**
     * Token 수정 / 처리
     * isComplete : false일 경우에는 수정, true일 경우에 처리
     *
     * @return Boolean
     */
    fun updateToken(restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.PUT_TOKEN_DATA.url.replace(restTemplate.getKeyRegex(), restTemplateTokenDto.tokenId))
        val responseEntity = restTemplate.update(url, restTemplateTokenDto)
        return responseEntity.body.toString().isNotEmpty()
        val result = restTemplate.update(url, restTemplateTokenDto)
        if (result) {
            restTemplateTokenDto.fileDataIds?.let { aliceFileService.uploadFiles(it) }
        }
        return result
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @return List<tokenDto>
     */
    fun getTokenList(): List<RestTemplateInstanceViewDto> {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params.add("userKey", aliceUserDto.userKey)
        params.add("status", RestTemplateConstants.TokenStatus.RUNNING.value)

        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_INSTANCES.url, parameters = params)
        val responseBody = restTemplate.get(url)

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val tokens: List<RestTemplateInstanceViewDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateInstanceViewDto::class.java))
        for (token in tokens) {
            token.createDt = token.createDt.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return tokens
    }

    /**
     * 처리할 문서 상세 조회.
     *
     * @return List<tokenDto>
     */
    fun findToken(tokenId: String): String {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.GET_TOKEN_DATA.url.replace(restTemplate.getKeyRegex(), tokenId))
        return restTemplate.get(url)
    }
}
