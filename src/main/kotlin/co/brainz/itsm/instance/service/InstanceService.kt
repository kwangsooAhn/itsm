/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.organization.dto.OrganizationSearchCondition
import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.organization.service.OrganizationService
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.calendar.service.CalendarDocumentService
import co.brainz.itsm.instance.dto.CommentDto
import co.brainz.itsm.instance.dto.InstanceCommentDto
import co.brainz.itsm.instance.dto.InstanceScheduleDto
import co.brainz.itsm.instance.dto.InstanceViewerListDto
import co.brainz.itsm.instance.dto.ViewerListDto
import co.brainz.itsm.instance.dto.ViewerListReturnDto
import co.brainz.itsm.instance.entity.WfCommentEntity
import co.brainz.itsm.instance.entity.WfInstanceViewerEntity
import co.brainz.itsm.instance.mapper.CommentMapper
import co.brainz.itsm.instance.repository.CommentRepository
import co.brainz.itsm.instance.repository.ViewerRepository
import co.brainz.itsm.user.service.UserService
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.token.service.WfTokenService
import java.time.LocalDateTime
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InstanceService(
    private val aliceUserRepository: AliceUserRepository,
    private val userDetailsService: AliceUserDetailsService,
    private val wfInstanceService: WfInstanceService,
    private val wfTokenService: WfTokenService,
    private val currentSessionUser: CurrentSessionUser,
    private val wfInstanceRepository: WfInstanceRepository,
    private val commentRepository: CommentRepository,
    private val organizationService: OrganizationService,
    private val organizationRepository: OrganizationRepository,
    private val viewerRepository: ViewerRepository,
    private val wfTokenManagerService: WfTokenManagerService,
    private val userService: UserService,
    private val calendarDocumentService: CalendarDocumentService
) {
    private val commentMapper: CommentMapper = Mappers.getMapper(CommentMapper::class.java)

    fun getInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto>? {
        return wfInstanceService.getInstancesHistory(instanceId)
    }

    fun getInstanceHistoryByTokenId(tokenId: String): List<RestTemplateInstanceHistoryDto>? {
        var histories: MutableList<RestTemplateInstanceHistoryDto>? = mutableListOf()

        getInstanceId(tokenId)?.let { instanceId ->
            histories = wfInstanceService.getInstancesHistory(instanceId)
        }
        return histories
    }

    fun getInstance(instanceId: String): RestTemplateInstanceDto {
        return wfInstanceService.instance(instanceId)
    }

    fun getInstanceId(tokenId: String): String? {
        return wfTokenService.getToken(tokenId).instanceId
    }

    fun getInstanceTags(instanceId: String): List<AliceTagDto> {
        return wfInstanceService.getInstanceTags(instanceId)
    }

    /**
     * [instanceId] 값으로 본인을 제외한 전체 문서 목록 조회 (관련된 문서 여부를 포함)
     */
    fun findAllInstanceListByRelatedCheck(
        instanceId: String,
        searchValue: String
    ): List<RestTemplateInstanceListDto>? {
        return wfInstanceService.findAllInstanceListByRelatedCheck(instanceId, searchValue)
    }

    /**
     * Get Instance Comments.
     */
    fun getComments(instanceId: String): List<InstanceCommentDto> {
        val commentList: MutableList<InstanceCommentDto> = mutableListOf()
        val commentEntities = commentRepository.findByInstanceId(instanceId)
        commentEntities.forEach { comment ->
            commentList.add(commentMapper.toCommentDto(comment))
        }
        val commentsDto = commentList.toList()
        val moreInfoAddCommentsDto: MutableList<InstanceCommentDto> = mutableListOf()
        commentsDto.forEach {
            val user = aliceUserRepository.getOne(it.createUserKey!!)
            val avatarPath = userDetailsService.makeAvatarPath(user)
            it.avatarPath = avatarPath
            moreInfoAddCommentsDto.add(it)
        }
        return moreInfoAddCommentsDto
    }

    /**
     * Set Init Instance.
     */
    fun setInitInstance(instanceId: String, documentId: String?): Boolean {
        var isSuccess = true
        val instance = wfInstanceRepository.findByInstanceId(instanceId)
        if (instance == null && !documentId.isNullOrEmpty()) {
            val token = WfTokenDto(
                documentId = documentId,
                instanceId = instanceId,
                assigneeId = currentSessionUser.getUserKey(),
                action = WfElementConstants.Action.SAVE.value
            )
            isSuccess = WfEngine(wfTokenManagerService).startWorkflow(token)
        }
        return isSuccess
    }

    /**
     * Set Comment.
     */
    @Transactional
    fun setComment(instanceId: String, commentDto: CommentDto): ZResponse {
        val createUserKey = currentSessionUser.getUserKey()
        val commentEntity = WfCommentEntity(
            commentId = "",
            content = commentDto.commentValue,
            createDt = LocalDateTime.now()
        )
        commentEntity.aliceUserEntity =
            aliceUserRepository.findAliceUserEntityByUserKey(createUserKey)
        if (this.setInitInstance(instanceId, commentDto.documentId)) {
            commentEntity.instance = wfInstanceRepository.findByInstanceId(instanceId)
            commentRepository.save(commentEntity)
        }
        return ZResponse()
    }

    /**
     * Delete Comment.
     */
    fun deleteComment(instanceId: String, commentId: String): ZResponse {
        val commentEntity = commentRepository.getOne(commentId)
        commentEntity.aliceUserEntity?.let { userService.userAccessAuthCheck(it.userKey, null) }
        commentRepository.deleteById(commentId)
        return ZResponse()
    }

    /**
     * Get Viewer list.
     */
    fun getInstanceViewerList(instanceId: String): ViewerListReturnDto {
        val viewerReturnList: MutableList<ViewerListDto> = mutableListOf()
        var count = 0L
        val viewerResults = viewerRepository.findByInstanceViewerList(instanceId)
        if (viewerResults != null) {
            count = viewerResults.size.toLong()
            for (viewer in viewerResults) {
                val organizationList = organizationRepository.findByOrganizationSearchList(OrganizationSearchCondition())
                val organization = organizationList.firstOrNull { it.organizationId == viewer.viewer.department }
                var organizationName = mutableListOf<String>()
                if (organization != null) {
                    if (organization.pOrganization != null) {
                        organizationName = organizationService.getOrganizationParent(organization, organizationList, organizationName)
                    } else {
                        organizationName.add(organization.organizationName.toString())
                    }
                }
                viewer.viewer.department = organizationName.joinToString(" > ")

                val avatarPath = userDetailsService.makeAvatarPath(viewer.viewer)
                viewerReturnList.add(
                    ViewerListDto(
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
        }

        return ViewerListReturnDto(
            data = viewerReturnList,
            totalCount = count
        )
    }

    /**
     * Add Viewers.
     */
    @Transactional
    fun createInstanceViewer(instanceId: String, instanceViewerListDto: InstanceViewerListDto): ZResponse {
        val isSuccess = this.setInitInstance(instanceId, instanceViewerListDto.documentId)
        if (isSuccess) {
            val instance = wfInstanceRepository.findByInstanceId(instanceId)
            viewerRepository.deleteWfInstanceViewerEntityByInstanceAndAndReviewYn(instance!!, false)
            instanceViewerListDto.viewers.forEach {
                val viewer = aliceUserRepository.findAliceUserEntityByUserKey(it.viewerKey)
                val instanceViewerEntity =
                    WfInstanceViewerEntity(
                        instance = instance,
                        viewer = viewer,
                        reviewYn = it.reviewYn,
                        displayYn = it.displayYn,
                        createUserKey = currentSessionUser.getUserKey(),
                        createDt = LocalDateTime.now()
                    )
                viewerRepository.save(instanceViewerEntity)
            }
        }
        return ZResponse(
            status = if (isSuccess) ZResponseConstants.STATUS.SUCCESS.code else ZResponseConstants.STATUS.ERROR_FAIL.code
        )
    }

    /**
     * Delete Viewer.
     */
    @Transactional
    fun deleteInstanceViewer(instanceId: String, viewerKey: String): ZResponse {
        val viewer = viewerRepository.findByInstanceIdAndViewerKey(instanceId, viewerKey)
        if (viewer != null) {
            viewerRepository.delete(viewer)
        }
        return ZResponse()
    }

    /**
     * Set Document Calendar Schedule.
     */
    @Transactional
    fun setSchedule(instanceId: String, instanceScheduleDto: InstanceScheduleDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val isSuccess = this.setInitInstance(instanceId, instanceScheduleDto.documentId)
        if (isSuccess) {
            status = calendarDocumentService.postDocumentSchedule(instanceScheduleDto)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * Delete Document Calendar Schedule.
     */
    @Transactional
    fun deleteSchedule(instanceId: String, scheduleId: String): ZResponse {
        val status = calendarDocumentService.deleteDocumentSchedule(instanceId, scheduleId)
        return ZResponse(
            status = status.code
        )
    }
}
