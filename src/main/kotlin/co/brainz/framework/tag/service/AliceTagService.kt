/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.service

import co.brainz.framework.tag.dto.AliceCustomTagDto
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.instance.service.InstanceService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class AliceTagService(
    private val aliceTagManager: AliceTagManager,
    private val instanceService: InstanceService
) {

    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Set Tag.
     */
    fun insertTag(aliceCustomTag: AliceCustomTagDto): String {
        var isSuccess = true
        if (aliceCustomTag.options != null) {
            val options: Map<String, Any> =
                mapper.convertValue(aliceCustomTag.options, object : TypeReference<Map<String, Any>>() {})
            if (options["documentId"] != null) {
                isSuccess = instanceService.setInitInstance(aliceCustomTag.targetId, options["documentId"].toString())
            }
        }
        var tagId = ""
        if (isSuccess) {
            val aliceTagDto = AliceTagDto(
                tagId = aliceCustomTag.tagId,
                tagType = aliceCustomTag.tagType,
                tagValue = aliceCustomTag.tagValue,
                targetId = aliceCustomTag.targetId
            )
            tagId = aliceTagManager.insertTag(aliceTagDto)
        }
        return tagId
    }

    /**
     * Delete Tag.
     */
    fun deleteTag(tagId: String) {
        return aliceTagManager.deleteTag(tagId)
    }

    /**
     * Get Tags by target ID.
     */
    fun getTagsByTargetId(tagType: String, targetId: String): List<AliceTagDto> {
        return aliceTagManager.getTagsByTargetId(tagType, targetId)
    }

    fun getSuggestionList(tagType: String, tagValue: String): List<String> {
        return aliceTagManager.getSuggestionList(tagType, tagValue)
    }
}
