package co.brainz.framework.label.repository

import co.brainz.framework.label.entity.AliceLabelEntity

interface AliceLabelRepositoryCustom {

    fun findLabels(labelTarget: String, targetId: String, labelKey: String): MutableList<AliceLabelEntity>
}
