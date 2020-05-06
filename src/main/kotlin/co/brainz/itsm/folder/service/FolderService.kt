package co.brainz.itsm.folder.service

import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class FolderService(private val restTemplate: RestTemplateProvider) {
    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getRelatedInstance(tokenId: String): List<RestTemplateRelatedInstanceDto>? {
        var relatedInstance: MutableList<RestTemplateRelatedInstanceDto>? = null
        val params = LinkedMultiValueMap<String, String>()
        params["tokenId"] = tokenId

        val urlDto =
            RestTemplateUrlDto(callUrl = RestTemplateConstants.Instance.GET_RELATED_INSTANCE.url, parameters = params)
        val responseBody = restTemplate.get(urlDto)

        relatedInstance = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateRelatedInstanceDto::class.java)
        )

        relatedInstance?.let { relatedInstance ->
            for (instance in relatedInstance) {
                instance.instanceStartDt = instance.instanceStartDt?.let { AliceTimezoneUtils().toTimezone(it) }
                instance.instanceEndDt = instance.instanceEndDt?.let { AliceTimezoneUtils().toTimezone(it) }
            }
        }

        return relatedInstance
    }
}
