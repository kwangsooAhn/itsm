/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.entity.QWfInstanceViewerEntity
import co.brainz.workflow.instance.entity.WfInstanceViewerEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfInstanceViewerRepositoryImpl : QuerydslRepositorySupport(WfInstanceViewerEntity::class.java), WfInstanceViewerRepositoryCustom {

    override fun existsByViewerKey(instanceId: String, userKey: String): Boolean {
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        return from(viewer)
            .where(
                viewer.instanceId.eq(instanceId)
                    .and(viewer.viewerKey.eq(userKey))
            )
            .fetchFirst() != null
    }
}
