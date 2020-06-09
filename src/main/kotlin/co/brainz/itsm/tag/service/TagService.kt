package co.brainz.itsm.tag.service

import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateTagDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TagService(
    private val restTemplate: RestTemplateProvider
) {

    /**
     * Set Tag.
     */
    fun setTag(restTemplateTagDto: RestTemplateTagDto): Boolean {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Tag.POST_TAG.url
        )
        val responseEntity = restTemplate.create(url, restTemplateTagDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * Delete Tag.
     */
    fun deleteTag(tagId: String): ResponseEntity<String> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Tag.DELETE_TAG.url.replace(
                restTemplate.getKeyRegex(),
                tagId
            )
        )
        return restTemplate.delete(url)
    }
}
