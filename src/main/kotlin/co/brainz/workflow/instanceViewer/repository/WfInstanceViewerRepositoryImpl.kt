/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.instanceViewer.repository

import javax.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.instanceViewer.entity.QWfInstanceViewerEntity
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import co.brainz.framework.auth.entity.QAliceUserEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections

@Repository
class WfInstanceViewerRepositoryImpl : QuerydslRepositorySupport(WfInstanceViewerEntity::class.java),
    WfInstanceViewerRepositoryCustom {

    override fun findByInstanceViewerList(instanceId: String): QueryResults<WfInstanceViewerEntity>? {
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity
        val user = QAliceUserEntity.aliceUserEntity

        return from(viewer)
            .where(
                viewer.instance.instanceId.eq(instanceId)
            )
            .orderBy(viewer.createDt.desc())
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

    @Transactional
    @Modifying
    override fun deleteByInstanceIdAndViewerKey(instanceId: String, viewerKey: String) {
        val viewer = QWfInstanceViewerEntity.wfInstanceViewerEntity

        delete(viewer)
            .where(
                viewer.instance.instanceId.`in`(instanceId)
                    .and(viewer.viewer.userKey.`in`(viewerKey))
            )
    }
}
