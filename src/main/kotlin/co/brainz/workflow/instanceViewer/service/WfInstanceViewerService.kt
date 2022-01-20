/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.instanceViewer.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.instanceViewer.constants.WfInstanceViewerConstants
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instanceViewer.dto.WfInstanceViewerDataDto
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import co.brainz.workflow.instanceViewer.repository.WfInstanceViewerRepository
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class WfInstanceViewerService(
    private val currentSessionUser: CurrentSessionUser,
    private val wfInstanceViewerRepository: WfInstanceViewerRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val wfInstanceRepository: WfInstanceRepository
) {
    @Transactional
    fun createInstanceViewer(wfInstanceViewerDataDtoList: List<WfInstanceViewerDataDto>, instanceId: String): Boolean {
        val instance = wfInstanceRepository.findByInstanceId(instanceId)

        wfInstanceViewerDataDtoList.forEach {
            val viewerKey = aliceUserRepository.findAliceUserEntityByUserKey(it.viewerKey)
            when(it.viewerType) {
                WfInstanceViewerConstants.ViewType.REGISTER.value -> {
                    val instanceViewerEntity =
                        WfInstanceViewerEntity(
                            instance = instance!!,
                            viewer = viewerKey,
                            reviewYn = it.reviewYn!!,
                            displayYn = it.displayYn,
                            createUserKey = currentSessionUser.getUserKey(),
                            createDt = LocalDateTime.now()
                        )
                    wfInstanceViewerRepository.save(instanceViewerEntity)
                }
                WfInstanceViewerConstants.ViewType.DELETE.value -> {
                    this.deleteInstanceViewer(instanceId, it.viewerKey)
                }
            }
        }
        return true
    }

    fun deleteInstanceViewer(instanceId: String, viewerKey: String): Boolean {
        wfInstanceViewerRepository.deleteByInstanceViewer(instanceId, viewerKey)
        return true
    }
}