/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label.repository

import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.label.entity.QAliceLabelEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class AliceLabelRepositoryImpl : QuerydslRepositorySupport(AliceLabelEntity::class.java),
    AliceLabelRepositoryCustom {

    val aliceLabelEntity: QAliceLabelEntity = QAliceLabelEntity.aliceLabelEntity

    override fun findLabels(
        labelTarget: String,
        targetId: String,
        labelKey: String
    ): MutableList<AliceLabelEntity> {

        var query = from(aliceLabelEntity)
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
            .where(aliceLabelEntity.labelKey.eq(labelKey))

        return query.fetch()
    }
}