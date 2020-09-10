package co.brainz.itsm.instance.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.avatar.service.AliceAvatarService
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class InstanceService(
    private val restTemplate: RestTemplateProvider,
    private val aliceUserRepository: AliceUserRepository,
    private val aliceAvatarService: AliceAvatarService
) {
    private val mapper: ObjectMapper =
        ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getInstanceHistory(tokenId: String): List<RestTemplateInstanceHistoryDto>? {
        var histories: MutableList<RestTemplateInstanceHistoryDto>? = mutableListOf()

        getInstanceId(tokenId)?.let { instanceId ->
            val urlDto = RestTemplateUrlDto(
                callUrl = RestTemplateConstants.Instance.GET_INSTANCE_HISTORY.url.replace(
                    restTemplate.getKeyRegex(),
                    instanceId
                )
            )
            histories = mapper.readValue(restTemplate.get(urlDto))
        }
        return histories
    }

    fun getInstance(instanceId: String): RestTemplateInstanceDto {
        val instanceUrlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Workflow.GET_INSTANCE.url.replace(
                restTemplate.getKeyRegex(),
                instanceId
            )
        )
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.readValue(
            restTemplate.get(instanceUrlDto),
            mapper.typeFactory.constructType(RestTemplateInstanceDto::class.java)
        )
    }

    fun getInstanceId(tokenId: String): String? {
        val tokenUrlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )

        val tokenDto: RestTemplateTokenDto = mapper.readValue(
            restTemplate.get(tokenUrlDto),
            mapper.typeFactory.constructType(RestTemplateTokenDto::class.java)
        )

        return tokenDto.instanceId
    }

    fun getInstanceComments(instanceId: String): List<RestTemplateCommentDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Instance.GET_INSTANCE_COMMENTS.url.replace(
                restTemplate.getKeyRegex(),
                instanceId
            )
        )
        val responseBody = restTemplate.get(url)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        val commentsDto: MutableList<RestTemplateCommentDto> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(
                List::class.java,
                RestTemplateCommentDto::class.java
            )
        )

        val moreInfoAddCommentsDto: MutableList<RestTemplateCommentDto> = mutableListOf()
        commentsDto.forEach {
            val user = aliceUserRepository.getOne(it.createUserKey!!)
            val avatarPath = aliceAvatarService.makeAvatarPath(user.avatar)
            it.avatarPath = avatarPath
            moreInfoAddCommentsDto.add(it)
        }
        return moreInfoAddCommentsDto
    }

    fun getInstanceTags(instanceId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Instance.GET_INSTANCE_TAGS.url.replace(
                restTemplate.getKeyRegex(),
                instanceId
            )
        )
        val responseBody = restTemplate.get(url)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val restTemplateTags: List<RestTemplateTagViewDto> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(
                List::class.java,
                RestTemplateTagViewDto::class.java
            )
        )
        val tagBasicList = JsonArray()
        restTemplateTags.forEach {
            val tagData = JsonObject()
            tagData.addProperty("id", it.id.toString())
            tagData.addProperty("value", it.value)
            tagBasicList.add(tagData)
        }
        return tagBasicList.toString()
    }

    fun getAllInstanceListAndSearch(
        instanceId: String,
        searchValue: String
    ): List<RestTemplateInstanceListDto>? {
        val params = LinkedMultiValueMap<String, String>()
        params["instanceId"] = instanceId
        params["searchValue"] = searchValue

        val urlDto =
            RestTemplateUrlDto(
                callUrl = RestTemplateConstants.Instance.GET_INSTANCE_SEARCH.url,
                parameters = params
            )
        val responseBody = restTemplate.get(urlDto)

        return mapper.readValue<MutableList<RestTemplateInstanceListDto>?>(
            responseBody,
            mapper.typeFactory.constructCollectionType(
                List::class.java,
                RestTemplateInstanceListDto::class.java
            )
        )
    }
}
