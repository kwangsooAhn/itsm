package co.brainz.workflow.tag.service

import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateTagDto
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
import co.brainz.workflow.tag.entity.WfTagEntity
import co.brainz.workflow.tag.entity.WfTagMapEntity
import co.brainz.workflow.tag.repository.WfTagDataRepository
import co.brainz.workflow.tag.repository.WfTagRepository
import org.springframework.stereotype.Service

@Service
class WfTagService(
    private val wfTagRepository: WfTagRepository,
    private val wfTagDataRepository: WfTagDataRepository,
    private val wfInstanceRepository: WfInstanceRepository
) {

    /**
     * Get Instance Tags.
     */
    fun getInstanceTags(instanceId: String): List<RestTemplateTagViewDto> {
        return wfTagRepository.findByInstanceId(instanceId)
    }

    /**
     * Insert Tag.
     */
    fun insertTag(restTemplateTagDto: RestTemplateTagDto): Boolean {
        val wfTagEntity =
            wfTagDataRepository.save(WfTagEntity(tagContent = restTemplateTagDto.tagContent))
        val wfTagMapEntity = WfTagMapEntity(
            tagId = wfTagEntity.tagId,
            instance = restTemplateTagDto.instanceId.let { wfInstanceRepository.findByInstanceId(it) }
        )
        wfTagRepository.save(wfTagMapEntity)

        return true
    }

    /**
     * Delete Tag.
     */
    fun deleteTag(tagId: String): Boolean {
        wfTagRepository.deleteById(tagId)
        wfTagDataRepository.deleteById(tagId)
        return true
    }
}
