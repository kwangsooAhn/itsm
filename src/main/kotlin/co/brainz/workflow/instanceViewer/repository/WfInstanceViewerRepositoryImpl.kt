/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.instanceViewer.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.instanceViewer.entity.QWfInstanceViewerEntity
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfInstanceViewerRepositoryImpl : QuerydslRepositorySupport(WfInstanceViewerEntity::class.java),
    WfInstanceViewerRepositoryCustom {

    override fun getReviewYnByViewKey(instanceId: String, userKey: String): WfInstanceViewerEntity? {
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        return from(viewer)
            .where(
                viewer.instance.instanceId.eq(instanceId)
                    .and(viewer.viewer.eq(AliceUserEntity(userKey)))
            )
            .fetchOne()
    }
}
