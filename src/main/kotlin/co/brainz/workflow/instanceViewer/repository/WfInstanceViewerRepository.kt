package co.brainz.workflow.instanceViewer.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity

@Repository
interface WfInstanceViewerRepository : JpaRepository<WfInstanceViewerEntity, String>,
    WfInstanceViewerRepositoryCustom {
    fun deleteByInstanceIdAndViewerKey(instanceId: String, viewerKey: String)
}