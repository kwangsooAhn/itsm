package co.brainz.itsm.instance.service

import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.itsm.instance.constants.InstanceConstants
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class InstanceService(private val restTemplate: RestTemplateProvider) {
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getInstanceHistory(tokenId: String): List<RestTemplateInstanceHistoryDto>? {
        var instanceHistory: MutableList<RestTemplateInstanceHistoryDto>? = null
        getInstanceId(tokenId)?.let { instanceId ->
            val urlDto = RestTemplateUrlDto(
                callUrl = RestTemplateConstants.Instance.GET_INSTANCE_HISTORY.url.replace(
                    restTemplate.getKeyRegex(),
                    instanceId
                )
            )
            val responseBody = restTemplate.get(urlDto)
            instanceHistory = mapper.readValue(
                responseBody,
                mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateInstanceHistoryDto::class.java)
            )
            instanceHistory?.let { instanceHistory ->
                for (instance in instanceHistory) {
                    if (InstanceConstants.InstanceHistory.isHistoryElement(instance.elementType)) {
                        instance.tokenStartDt = instance.tokenStartDt?.let { AliceTimezoneUtils().toTimezone(it) }
                        instance.tokenEndDt = instance.tokenEndDt?.let { AliceTimezoneUtils().toTimezone(it) }
                    } else {
                        instanceHistory.remove(instance)
                    }
                }
            }
        }

        return instanceHistory
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

        val restTemplateComments: List<RestTemplateCommentDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateCommentDto::class.java))
        for (comment in restTemplateComments) {
            comment.createDt = comment.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }

        return restTemplateComments
    }

    fun getInstanceList(instanceId: String, searchValue: String) : List<RestTemplateInstanceListDto>? {
        var instanceList: MutableList<RestTemplateInstanceListDto>? = null
        val params = LinkedMultiValueMap<String, String>()
        params["instanceId"] = instanceId
        params["searchValue"] = searchValue

        val urlDto =
            RestTemplateUrlDto(callUrl = RestTemplateConstants.Instance.GET_INSTANCE_LIST.url, parameters = params)
        val responseBody = restTemplate.get(urlDto)

        instanceList = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateInstanceListDto::class.java)
        )

        instanceList?.let { instanceList ->
            for (instance in instanceList) {
                instance.instanceStartDt = instance.instanceStartDt?.let { AliceTimezoneUtils().toTimezone(it) }
                instance.instanceEndDt = instance.instanceEndDt?.let { AliceTimezoneUtils().toTimezone(it) }
            }
        }

        return instanceList
    }
}
