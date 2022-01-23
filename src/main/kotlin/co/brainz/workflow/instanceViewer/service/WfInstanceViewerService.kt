/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.instanceViewer.service

import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.instanceViewer.constants.WfInstanceViewerConstants
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instanceViewer.dto.WfInstanceViewerDataDto
import co.brainz.workflow.instanceViewer.dto.WfInstanceViewerListDto
import co.brainz.workflow.instanceViewer.dto.WfInstanceViewerListReturnDto
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import co.brainz.workflow.instanceViewer.repository.WfInstanceViewerRepository
import com.querydsl.core.QueryResults
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class WfInstanceViewerService(
    private val currentSessionUser: CurrentSessionUser,
    private val wfInstanceViewerRepository: WfInstanceViewerRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val userDetailsService: AliceUserDetailsService
) {
    fun getInstanceViewerList(instanceId: String): WfInstanceViewerListReturnDto? {
        val viewerReturnList: MutableList<WfInstanceViewerListDto> = mutableListOf()
        val queryResults: QueryResults<WfInstanceViewerEntity>? =
            wfInstanceViewerRepository.findByInstanceViewerList(instanceId)
        if (queryResults != null) {
            val viewerList = queryResults.results
            val count: Long = viewerList.size.toLong()
            
            for (viewer in viewerList) {
                val avatarPath = userDetailsService.makeAvatarPath(viewer.viewer)
                viewerReturnList.add(
                    WfInstanceViewerListDto(
                        instanceId = viewer.instance.instanceId,
                        viewerKey = viewer.viewer.userKey,
                        viewerName = viewer.viewer.userName,
                        organizationName = viewer.viewer.department,
                        avatarPath = avatarPath,
                        reviewYn = viewer.reviewYn,
                        displayYn = viewer.displayYn,
                        createUserKey = viewer.createUserKey,
                        createDt = viewer.createDt,
                        updateUserKey = viewer.updateUserKey,
                        updateDt = viewer.updateDt
                    )
                )
            }

            return WfInstanceViewerListReturnDto(
                data = viewerReturnList,
                totalCount = count
            )
        }
        return null
    }

    @Transactional
    fun createInstanceViewer(wfInstanceViewerDataDtoList: List<WfInstanceViewerDataDto>, instanceId: String): Boolean {
        val instance = wfInstanceRepository.findByInstanceId(instanceId)

        wfInstanceViewerDataDtoList.forEach {
            val viewer = aliceUserRepository.findAliceUserEntityByUserKey(it.viewerKey)
            val documentId = it.documentId  //오류나서 임시 저장하면서 사용
            when (it.viewerType) {
                WfInstanceViewerConstants.ViewType.REGISTER.value -> {
                    val instanceViewerEntity =
                        WfInstanceViewerEntity(
                            instance = instance!!,
                            viewer = viewer,
                            reviewYn = it.reviewYn,
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
        wfInstanceViewerRepository.deleteByInstanceIdAndViewerKey(instanceId, viewerKey)
        return true
    }
}
