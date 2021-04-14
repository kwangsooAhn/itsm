package co.brainz.framework.tag.repository

import co.brainz.framework.tag.dto.AliceTagDto

interface AliceTagRepositoryCustom {
    fun findByTargetId(tagType: String, targetId: String): List<AliceTagDto>
    fun findByTargetIds(tagType: String, targetIds: Set<String>): List<AliceTagDto>
}
