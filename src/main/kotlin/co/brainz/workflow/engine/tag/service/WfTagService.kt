package co.brainz.workflow.engine.tag.service

import co.brainz.workflow.engine.tag.entity.WfTagEntity
import co.brainz.workflow.engine.tag.entity.WfTagDataEntity
import co.brainz.workflow.engine.tag.repository.WfTagRepository
import co.brainz.workflow.engine.tag.repository.WfTagDataRepository
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateTagDto
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
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
        val wfTagEntity = WfTagEntity(
            tagId = ""
        )
        wfTagEntity.instance =
            restTemplateTagDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) }
        val dataEntity = wfTagRepository.save(wfTagEntity)
        val wfTagDataEntity = WfTagDataEntity(
            tagId = dataEntity.tagId,
            tagContent = restTemplateTagDto.tagContent
        )
        wfTagDataRepository.save(wfTagDataEntity)
        return true
    }

    /**
     * Delete Tag.
     */
    fun deleteTag(tagId: String): Boolean {
        wfTagRepository.deleteById(tagId)
        return true
    }
}
