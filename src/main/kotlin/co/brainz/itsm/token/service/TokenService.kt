package co.brainz.itsm.token.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateTokenSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.mapstruct.factory.Mappers
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TokenService(
    private val restTemplate: RestTemplateProvider,
    private val aliceFileService: AliceFileService
) {

    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    /**
     * Post Token 처리.
     *
     * @param restTemplateTokenDto
     * @return Boolean
     */
    fun postToken(restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateTokenDto.instanceCreateUser = userMapper.toAliceUserEntity(aliceUserDto)
        restTemplateTokenDto.assigneeId = aliceUserDto.userKey
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.POST_TOKEN.url)
        val responseEntity = restTemplate.create(url, restTemplateTokenDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                restTemplateTokenDto.fileDataIds?.let { aliceFileService.uploadFiles(it) }
                true
            }
            false -> false
        }
    }

    /**
     * Put Token 처리.
     *
     * @param restTemplateTokenDto
     * @return Boolean
     */
    fun putToken(tokenId: String, restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.PUT_TOKEN.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateTokenDto.instanceCreateUser = userMapper.toAliceUserEntity(aliceUserDto)
        restTemplateTokenDto.assigneeId = aliceUserDto.userKey
        val responseEntity = restTemplate.update(url, restTemplateTokenDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                restTemplateTokenDto.fileDataIds?.let { aliceFileService.uploadFiles(it) }
                true
            }
            false -> false
        }
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @param restTemplateTokenSearchListDto
     * @return List<tokenDto>
     */
    fun getTokenList(restTemplateTokenSearchListDto: RestTemplateTokenSearchListDto): List<RestTemplateInstanceViewDto> {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params.add("userKey", aliceUserDto.userKey)
        params.add("tokenType", restTemplateTokenSearchListDto.searchTokenType)
        params.add("documentId", restTemplateTokenSearchListDto.searchDocumentId)
        params.add("searchValue", restTemplateTokenSearchListDto.searchValue)
        params.add("offset", restTemplateTokenSearchListDto.offset)
        val dateTimeFormatter = DateTimeFormatter.ofPattern(aliceUserDto.timeFormat.split(" ")[0] + " HH:mm:ss")
        val fromGMT = AliceTimezoneUtils().toGMT(LocalDateTime.parse(restTemplateTokenSearchListDto.searchFromDt + " 00:00:00", dateTimeFormatter))
        val toGMT = AliceTimezoneUtils().toGMT(LocalDateTime.parse(restTemplateTokenSearchListDto.searchToDt + " 23:59:59", dateTimeFormatter))
        params.add("fromDt", dateTimeFormatter.format(fromGMT))
        params.add("toDt", dateTimeFormatter.format(toGMT))
        params.add("dateFormat", aliceUserDto.timeFormat.split(" ")[0] + " HH24:MI:SS")

        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_INSTANCES.url, parameters = params)
        val responseBody = restTemplate.get(url)

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val tokens: List<RestTemplateInstanceViewDto> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateInstanceViewDto::class.java)
        )
        for (token in tokens) {
            token.createDt = token.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return tokens
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
