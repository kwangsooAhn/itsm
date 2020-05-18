package co.brainz.itsm.folder.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
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

    fun getFolderId(tokenId: String): String?  {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Folder.GET_FOLDER.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )

        val restTemplateFolderDto: RestTemplateFolderDto = mapper.readValue(
            restTemplate.get(url),
            mapper.typeFactory.constructType(RestTemplateFolderDto::class.java)
        )

        return restTemplateFolderDto.folderId
    }

    fun createFolder(restTemplateFolderDto: List<RestTemplateFolderDto>): Boolean {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto

        for(i in restTemplateFolderDto) {
            i.createUserKey = aliceUserDto.userKey
        }

        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Folder.POST_FOLDER.url
        )
        val responseEntity = restTemplate.create(url, restTemplateFolderDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    fun deleteFolder(restTemplateFolderDto: RestTemplateFolderDto): Boolean {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Folder.DELETE_FOLDER.url
        )
        val responseEntity = restTemplate.delete(url, restTemplateFolderDto)
        return responseEntity.body.toString().isNotEmpty()
    }
}
