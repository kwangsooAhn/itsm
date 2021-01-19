/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapPk
import co.brainz.framework.auth.repository.AliceRoleAuthMapRepositoryCustom
import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.label.entity.AliceLabelEntityPk
import co.brainz.framework.label.entity.QAliceLabelEntity.aliceLabelEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AliceLabelRepository : JpaRepository<AliceLabelEntity, AliceLabelEntityPk>,
    AliceLabelRepositoryCustom {

    @Query("DELETE FROM AliceLabelEntity label WHERE label.labelTarget = :labelType AND label.labelTargetId = :labelTargetId")
    fun removeLabels(labelTarget: String, labelTargetId: String): Boolean
}