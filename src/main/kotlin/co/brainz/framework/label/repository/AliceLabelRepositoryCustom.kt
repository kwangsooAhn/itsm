package co.brainz.framework.label.repository

import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface AliceLabelRepositoryCustom : AliceRepositoryCustom {
    fun findLabels(labelTarget: String, targetId: String, labelKey: String?): MutableList<AliceLabelEntity>
}
