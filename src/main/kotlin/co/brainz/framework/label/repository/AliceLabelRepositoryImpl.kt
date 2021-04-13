/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label.repository

import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.label.entity.QAliceLabelEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class AliceLabelRepositoryImpl : QuerydslRepositorySupport(AliceLabelEntity::class.java),
    AliceLabelRepositoryCustom {

    val aliceLabelEntity: QAliceLabelEntity = QAliceLabelEntity.aliceLabelEntity

    /**
     * Label 조회.
     */
    override fun findLabels(
        labelTarget: String,
        targetId: String,
        labelKey: String?
    ): MutableList<AliceLabelEntity> {
        return from(aliceLabelEntity)
            .select(
                Projections.constructor(
                    AliceLabelEntity::class.java,
                    aliceLabelEntity.labelTarget,
                    aliceLabelEntity.labelTargetId,
                    aliceLabelEntity.labelKey,
                    aliceLabelEntity.labelValue
                )
            )
            .where(aliceLabelEntity.labelTarget.eq(labelTarget))
            .where(aliceLabelEntity.labelTargetId.eq(targetId))
            .where(labelKeyEq(labelKey))
            .fetch()
    }

    fun labelKeyEq(labelKey: String?): BooleanExpression? {
        return labelKey?.let {
            aliceLabelEntity.labelKey.eq(labelKey)
        } ?: run {
            null
        }
    }

    /**
     * Label Key를 통해 관련 라벨 데이터 조회
     */
    override fun findLabelByKeyIn(labelKey: ArrayList<String>): MutableList<AliceLabelEntity> {
        return from(aliceLabelEntity)
            .select(
                Projections.constructor(
                    AliceLabelEntity::class.java,
                    aliceLabelEntity.labelTarget,
                    aliceLabelEntity.labelTargetId,
                    aliceLabelEntity.labelKey,
                    aliceLabelEntity.labelValue
                )
            ).where(aliceLabelEntity.labelKey.`in`(labelKey)).fetch()
    }
}
