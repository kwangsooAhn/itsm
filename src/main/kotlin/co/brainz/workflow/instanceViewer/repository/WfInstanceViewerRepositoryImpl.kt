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

    override fun findViewerByInstanceId(instanceId: String): MutableList<WfInstanceViewerEntity> {
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        return from(viewer)
            .where(
                viewer.instance.instanceId.eq(instanceId)
                    .and(viewer.displayYn.eq(false))
            )
            .fetch()
    }

    override fun updateDisplayYn(instanceId: String, viewerKey: String) {
        val instanceViewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        update(instanceViewer)
            .where(instanceViewer.instance.instanceId.eq(instanceId)
                .and(instanceViewer.viewer.eq(AliceUserEntity(viewerKey))))
            .set(instanceViewer.displayYn, true)
            .execute()
    }
}
