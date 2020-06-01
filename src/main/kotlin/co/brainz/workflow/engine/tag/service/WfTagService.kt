package co.brainz.workflow.engine.tag.service

import co.brainz.workflow.engine.tag.entity.WfTagEntity
import co.brainz.workflow.engine.tag.entity.WfTagDataEntity
import co.brainz.workflow.engine.tag.mapper.WfTagMapper
import co.brainz.workflow.engine.tag.repository.WfTagRepository
import co.brainz.workflow.engine.tag.repository.WfTagDataRepository
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateTagDto
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class WfTagService(
    private val wfTagRepository: WfTagRepository,
    private val wfTagDataRepository: WfTagDataRepository,
    private val wfInstanceRepository: WfInstanceRepository
) {

    private val wfCommentMapper: WfTagMapper = Mappers.getMapper(WfTagMapper::class.java)

    /**
     * Get Instance Tags.
     */
    fun getInstanceTags(instanceId: String): MutableList<RestTemplateTagDto> {
        val tagList: MutableList<RestTemplateTagDto> = mutableListOf()
        val tagEntities = wfTagDataRepository.findByInstanceId(instanceId)
        tagEntities.forEach { tag ->
            tagList.add(wfCommentMapper.toTagDto(tag))
        }

        return tagList
    }

    /**
     * Insert Tag.
     */
    fun insertTag(restTemplateTagDto: RestTemplateTagDto): Boolean {
        // 태그 정보 저장
        val wfTagEntity = WfTagEntity(
            tagId = "",
            tagContent = restTemplateTagDto.tagContent
        )
        wfTagRepository.save(wfTagEntity)
        // 태그 매핑 정보 저장
        val wfTagDataEntity = WfTagDataEntity(
            tagId = ""
        )
        wfTagDataEntity.instance = restTemplateTagDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) }
//        wfTagDataRepository.save(wfTagDataEntity)
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
