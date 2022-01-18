package co.brainz.workflow.instanceViewer.service

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.instanceViewer.dto.InstanceViewerDto
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import co.brainz.workflow.instanceViewer.repository.WfInstanceViewerRepository

@Service
class WfInstanceViewerService(
    private val currentSessionUser: CurrentSessionUser,
    private val wfInstanceViewerRepository: WfInstanceViewerRepository
) {

    fun createInstanceViewer(instanceViewerDto: InstanceViewerDto): Boolean {
        var instanceViewerEntity = WfInstanceViewerEntity(
            instance = instanceViewerDto.instanceId,
            viewer = instanceViewerDto.viewerKey,
            reviewYn = instanceViewerDto.reviewYn,
            displayYn = instanceViewerDto.displayYn,
            createUserKey = currentSessionUser.getUserKey(),
            createDt = LocalDateTime.now()
        )
        wfInstanceViewerRepository.save(instanceViewerEntity)
        return true
    }

    fun deleteInstanceViewer(instanceId: String, viewerKey: String): Boolean {
        wfInstanceViewerRepository.deleteByInstanceIdAndViewerKey(instanceId, viewerKey)
        return true
    }
}