/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.tag.dto.AliceTagDto

interface AliceTagRepositoryCustom : AliceRepositoryCustom {
    fun findByTargetId(tagType: String, targetId: String): List<AliceTagDto>
    fun findByTargetIds(tagType: String, targetIds: Set<String>): List<AliceTagDto>
    fun findSuggestionList(tagValue: String, tagType: String): List<String>
}
