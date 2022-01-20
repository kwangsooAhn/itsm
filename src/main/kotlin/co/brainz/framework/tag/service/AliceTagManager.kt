/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.service

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.entity.AliceTagEntity
import co.brainz.framework.tag.repository.AliceTagRepository
import org.springframework.stereotype.Component

@Component
class AliceTagManager(
    private val aliceTagRepository: AliceTagRepository
) {

    /**
     * Set Tag.
     */
    fun insertTag(aliceTagDto: AliceTagDto): String {
        val newTag = aliceTagRepository.save(
            AliceTagEntity(
                tagType = aliceTagDto.tagType,
                tagValue = aliceTagDto.tagValue,
                targetId = aliceTagDto.targetId
            )
        )
        return newTag.tagId
    }

    /**
     * Get Tags by target ID.
     */
    fun getTagsByTargetId(tagType: String, targetId: String): List<AliceTagDto> {
        return aliceTagRepository.findByTargetId(tagType, targetId)
    }

    /**
     * Get Tags by target IDs
     */
    fun getTagsByTargetIds(tagType: String, targetIds: Set<String>): List<AliceTagDto> {
        return aliceTagRepository.findByTargetIds(tagType, targetIds)
    }

    /**
     * Delete Tag.
     */
    fun deleteTag(tagId: String) {
        return aliceTagRepository.deleteById(tagId)
    }

    fun getSuggestionList(tagType: String, tagValue: String): List<String> {
        return aliceTagRepository.findSuggestionList(tagType, tagValue)
    }
}
