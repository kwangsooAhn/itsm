package co.brainz.workflow.instanceViewer.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import co.brainz.workflow.instance.repository.WfInstanceRepositoryCustom
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity

@Repository
interface WfInstanceViewerRepository : JpaRepository<WfInstanceViewerEntity, String>,
    WfInstanceRepositoryCustom {
    fun deleteByInstanceIdAndViewerKey(instanceId: String, viewerKey: String)
}