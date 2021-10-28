/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.folder.service.FolderService
import co.brainz.itsm.instance.dto.InstanceCommentDto
import co.brainz.itsm.instance.entity.WfCommentEntity
import co.brainz.itsm.instance.mapper.CommentMapper
import co.brainz.itsm.instance.repository.CommentRepository
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.token.service.WfTokenService
import java.time.LocalDateTime
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class InstanceService(
    private val aliceUserRepository: AliceUserRepository,
    private val userDetailsService: AliceUserDetailsService,
    private val folderService: FolderService,
    private val wfInstanceService: WfInstanceService,
    private val wfTokenService: WfTokenService,
    private val currentSessionUser: CurrentSessionUser,
    private val wfInstanceRepository: WfInstanceRepository,
    private val commentRepository: CommentRepository
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

    fun getAllInstanceListAndSearch(
        tokenId: String,
        searchValue: String
    ): List<RestTemplateInstanceListDto>? {
        val allInstanceList = wfInstanceService.getAllInstanceListAndSearch(this.getInstanceId(tokenId)!!, searchValue)

        if (!allInstanceList.isNullOrEmpty()) {
            val relatedInstanceList = folderService.getFolderByTokenId(tokenId)
            val relatedInstanceIds = mutableListOf<String>()
            relatedInstanceList?.forEach { relatedInstance ->
                relatedInstance.instanceId?.let { relatedInstanceIds.add(it) }
            }
            if (relatedInstanceIds.isNotEmpty()) {
                for (instance in allInstanceList) {
                    if (relatedInstanceIds.contains(instance.instanceId)) {
                        instance.related = true
                    }
                }
            }
        }
        return allInstanceList
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
     * Set Comment.
     */
    fun setComment(instanceId: String, contents: String): Boolean {
        val createUserKey = currentSessionUser.getUserKey()
        val commentEntity = WfCommentEntity(
            commentId = "",
            content = contents,
            createDt = LocalDateTime.now()
        )
        commentEntity.aliceUserEntity =
            aliceUserRepository.findAliceUserEntityByUserKey(createUserKey)
        commentEntity.instance = wfInstanceRepository.findByInstanceId(instanceId)
        commentRepository.save(commentEntity)
        return true
    }

    /**
     * Delete Comment.
     */
    fun deleteComment(instanceId: String, commentId: String): Boolean {
        commentRepository.deleteById(commentId)
        return true
    }
}
