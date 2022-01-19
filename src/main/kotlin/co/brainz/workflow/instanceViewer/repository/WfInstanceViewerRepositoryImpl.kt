package co.brainz.workflow.instanceViewer.repository

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import co.brainz.workflow.instanceViewer.dto.InstanceViewerDetailDto
import co.brainz.workflow.instanceViewer.entity.QWfInstanceViewerEntity
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import com.querydsl.core.types.Projections

@Repository
class WfInstanceViewerRepositoryImpl: QuerydslRepositorySupport(WfInstanceViewerEntity::class.java), WfInstanceViewerRepositoryCustom {
    override fun findInstanceViewer(instanceId: String, viewer: String): InstanceViewerDetailDto {
        val instanceView = QWfInstanceViewerEntity.wfInstanceViewerEntity

        return from(instanceView)
            .select(
                Projections.constructor(
                    InstanceViewerDetailDto::class.java,
                    instanceView.instance.instanceId,
                    instanceView.viewer.userKey,
                    instanceView.reviewYn,
                    instanceView.displayYn,
                    instanceView.createUserKey,
                    instanceView.createDt,
                    instanceView.updateUserKey,
                    instanceView.updateDt
                )
            )
            .where(
                instanceView.instance.instanceId.eq(instanceId)
                    .and(instanceView.viewer.userKey.eq(viewer))
            )
            .fetchOne()
    }
}