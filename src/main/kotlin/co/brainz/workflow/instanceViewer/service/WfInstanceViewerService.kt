/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.instanceViewer.service

import java.time.LocalDateTime
import javax.transaction.Transactional
import org.springframework.stereotype.Service
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.instanceViewer.dto.ViewerDto
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import co.brainz.workflow.instanceViewer.repository.WfInstanceViewerRepository

@Service
class WfInstanceViewerService(
    private val currentSessionUser: CurrentSessionUser,
    private val wfInstanceViewerRepository: WfInstanceViewerRepository
) {
    @Transactional
    fun createInstanceViewer(viewerDtoList: List<ViewerDto>): Boolean {
        viewerDtoList.forEach {
            if (!wfInstanceViewerRepository.findInstanceViewer(it.instanceId.instanceId, it.viewerKey.userKey).equals(null)) {
                val instanceViewerEntity = WfInstanceViewerEntity(
                    instance = it.instanceId,
                    viewer = it.viewerKey,
                    reviewYn = it.reviewYn,
                    displayYn = it.displayYn,
                    createUserKey = currentSessionUser.getUserKey(),
                    createDt = LocalDateTime.now()
                )
                wfInstanceViewerRepository.save(instanceViewerEntity)
            }
        }
        return true
    }

    fun deleteInstanceViewer(instanceId: String, viewerKey: String): Boolean {
        wfInstanceViewerRepository.deleteByInstanceIdAndViewerKey(instanceId, viewerKey)
        return true
    }
}