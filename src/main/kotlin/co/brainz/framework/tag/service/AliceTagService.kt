package co.brainz.framework.tag.service

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.entity.AliceTagEntity
import co.brainz.framework.tag.repository.AliceTagRepository
import org.springframework.stereotype.Service

@Service
class AliceTagService(
    private val aliceTagRepository: AliceTagRepository
) {

    /**
     * Set Tag.
     */
    fun insertTag(aliceTagDto: AliceTagDto): Boolean {
        aliceTagRepository.save(
            AliceTagEntity(
                tagType = aliceTagDto.tagType,
                tagValue = aliceTagDto.tagValue,
                targetId = aliceTagDto.targetId
            )
        )
        return true
    }

    /**
     * Delete Tag.
     */
    fun deleteTag(tagId: String) {
        return aliceTagRepository.deleteById(tagId)
    }

    /**
     * Get Tags by target ID.
     */
    fun getTagsByTargetId(tagType: String, targetId: String): List<AliceTagDto> {
        return aliceTagRepository.findByTargetId(tagType, targetId)
    }
}
