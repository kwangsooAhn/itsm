package co.brainz.workflow.instanceViewer.repository

import co.brainz.workflow.instanceViewer.dto.InstanceViewerDetailDto

interface WfInstanceViewerRepositoryCustom {
    fun findInstanceViewer(instanceId: String, viewer: String): InstanceViewerDetailDto
}