/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.instance.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.instance.entity.QWfInstanceViewerEntity
import co.brainz.itsm.instance.entity.WfInstanceViewerEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ViewerRepositoryImpl : QuerydslRepositorySupport(WfInstanceViewerEntity::class.java),
    ViewerRepositoryCustom {

    override fun findByInstanceViewerList(instanceId: String): QueryResults<WfInstanceViewerEntity>? {
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        return from(viewer)
            .where(
                viewer.instance.instanceId.eq(instanceId)
            )
            .orderBy(viewer.createDt.asc())
            .fetchResults()

    }

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
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        update(viewer)
            .where(
                viewer.instance.instanceId.eq(instanceId)
                    .and(viewer.viewer.eq(AliceUserEntity(viewerKey)))
            )
            .set(viewer.displayYn, true)
            .execute()
    }

    override fun findByInstanceIdAndViewerKey(instanceId: String, viewerKey: String): WfInstanceViewerEntity? {
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        return from(viewer)
            .where(
                viewer.instance.instanceId.eq(instanceId)
                    .and(viewer.viewer.userKey.eq(viewerKey))
            ).fetchOne()
    }

    override fun updateReviewYn(instanceId: String, viewerKey: String) {
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        update(viewer)
            .where(
                viewer.instance.instanceId.eq(instanceId)
                    .and(viewer.viewer.eq(AliceUserEntity(viewerKey)))
            )
            .set(viewer.reviewYn, true)
            .execute()
    }
}
