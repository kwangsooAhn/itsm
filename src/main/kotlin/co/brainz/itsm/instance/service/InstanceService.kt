package co.brainz.itsm.instance.service

import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.itsm.instance.constants.InstanceConstants
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class InstanceService(private val restTemplate: RestTemplateProvider) {
	private val mapper: ObjectMapper =
		ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

	fun getInstanceHistory(tokenId: String): List<RestTemplateInstanceHistoryDto>? {
		val rebindHistory: MutableList<RestTemplateInstanceHistoryDto> = mutableListOf()

		getInstanceId(tokenId)?.let { instanceId ->
			val urlDto = RestTemplateUrlDto(
				callUrl = RestTemplateConstants.Instance.GET_INSTANCE_HISTORY.url.replace(
					restTemplate.getKeyRegex(),
					instanceId
				)
			)
			val responseBody = restTemplate.get(urlDto)
			val histories: MutableList<RestTemplateInstanceHistoryDto>? =
				mapper.readValue(responseBody)
			histories?.forEach { history ->
				if (InstanceConstants.InstanceHistory.isHistoryElement(history.elementType)) {
					history.tokenStartDt =
						history.tokenStartDt?.let { AliceTimezoneUtils().toTimezone(it) }
					history.tokenEndDt =
						history.tokenEndDt?.let { AliceTimezoneUtils().toTimezone(it) }
					rebindHistory.add(history)
				}
			}
		}

		return rebindHistory
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

		val restTemplateComments: List<RestTemplateCommentDto> = mapper.readValue(
			responseBody,
			mapper.typeFactory.constructCollectionType(
				List::class.java,
				RestTemplateCommentDto::class.java
			)
		)
		for (comment in restTemplateComments) {
			comment.createDt = comment.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
		}

		return restTemplateComments
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
			tagData.addProperty("value", it.value.toString())
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

		val instanceList: MutableList<RestTemplateInstanceListDto>? = mapper.readValue(
			responseBody,
			mapper.typeFactory.constructCollectionType(
				List::class.java,
				RestTemplateInstanceListDto::class.java
			)
		)

		instanceList?.forEach { instance ->
			instance.instanceStartDt =
				instance.instanceStartDt?.let { AliceTimezoneUtils().toTimezone(it) }
			instance.instanceEndDt =
				instance.instanceEndDt?.let { AliceTimezoneUtils().toTimezone(it) }
		}

		return instanceList
	}
}
