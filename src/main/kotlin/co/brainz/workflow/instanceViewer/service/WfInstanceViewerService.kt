/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.instanceViewer.service

import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.organization.dto.OrganizationSearchCondition
import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.user.service.UserService
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
    private val userDetailsService: AliceUserDetailsService,
    private val organizationRepository: OrganizationRepository,
    private val userService: UserService
) {
    fun getInstanceViewerList(instanceId: String): WfInstanceViewerListReturnDto? {
        val viewerReturnList: MutableList<WfInstanceViewerListDto> = mutableListOf()
        val viewerResults: QueryResults<WfInstanceViewerEntity>? =
            wfInstanceViewerRepository.findByInstanceViewerList(instanceId)
        if (viewerResults != null) {
            val viewerList = viewerResults.results
            val count: Long = viewerList.size.toLong()
            for (viewer in viewerList) {
                val organizationList = organizationRepository.findByOrganizationSearchList(OrganizationSearchCondition())
                val organization = organizationList.results.firstOrNull { it.organizationId == viewer.viewer.department }
                var organizationName = mutableListOf<String>()
                if (organization != null) {
                    if (organization.pOrganization != null) {
                        organizationName = userService.getRecursive(organization, organizationList.results, organizationName)
                    } else {
                        organizationName.add(organization.organizationName.toString())
                    }
                }
                viewer.viewer.department = organizationName.joinToString(" > ")

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

    @Transactional
    fun deleteInstanceViewer(instanceId: String, viewerKey: String): Boolean {
        wfInstanceViewerRepository.delete(
            wfInstanceViewerRepository.findByInstanceIdAndViewerKey(instanceId, viewerKey)!!
        )
        return true
    }
}
