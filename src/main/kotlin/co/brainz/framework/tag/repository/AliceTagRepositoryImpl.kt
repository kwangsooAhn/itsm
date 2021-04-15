/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.repository

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.entity.AliceTagEntity
import co.brainz.framework.tag.entity.QAliceTagEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class AliceTagRepositoryImpl : QuerydslRepositorySupport(AliceTagEntity::class.java),
    AliceTagRepositoryCustom {

    val tag: QAliceTagEntity = QAliceTagEntity.aliceTagEntity

    override fun findByTargetId(tagType: String, targetId: String): List<AliceTagDto> {
        return from(tag)
            .select(
                Projections.constructor(
                    AliceTagDto::class.java,
                    tag.tagId,
                    tag.tagType,
                    tag.tagValue,
                    tag.targetId
                )
            )
            .where(
                (tag.targetId.eq(targetId))
                    .and(tag.tagType.eq(tagType))
            )
            .fetch()
    }

    override fun findByTargetIds(tagType: String, targetIds: Set<String>): List<AliceTagDto> {
        return from(tag)
            .select(
                Projections.constructor(
                    AliceTagDto::class.java,
                    tag.tagId,
                    tag.tagType,
                    tag.tagValue,
                    tag.targetId
                )
            )
            .where(
                (tag.targetId.`in`(targetIds))
                    .and(tag.tagType.eq(tagType))
            )
            .fetch()
    }
}
