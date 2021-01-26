/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label.repository

import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.label.entity.AliceLabelEntityPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AliceLabelRepository : JpaRepository<AliceLabelEntity, AliceLabelEntityPk>,
    AliceLabelRepositoryCustom {

    // 라벨 대상과 대상 아이디만을 이용해서 거기에 달린 라벨을 다 지우는 경우.
    @Query(
        "DELETE FROM AliceLabelEntity label WHERE label.labelTarget = :labelType " +
                "AND label.labelTargetId = :labelTargetId"
    )
    fun deleteLabels(labelTarget: String, labelTargetId: String): Boolean
}