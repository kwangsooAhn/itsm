package co.brainz.itsm.tag.service

import co.brainz.workflow.provider.dto.RestTemplateTagDto
import co.brainz.workflow.tag.service.WfTagService
import org.springframework.stereotype.Service

@Service
class TagService(
    private val wfTagService: WfTagService
) {

    /**
     * Set Tag.
     */
    fun setTag(restTemplateTagDto: RestTemplateTagDto): Boolean {
        return wfTagService.insertTag(restTemplateTagDto)
    }

    /**
     * Delete Tag.
     */
    fun deleteTag(tagId: String): Boolean {
        return wfTagService.deleteTag(tagId)
    }
}
