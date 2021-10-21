/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.itsm.comment.dto.InstanceCommentDto
import co.brainz.itsm.comment.service.CommentService
import co.brainz.itsm.folder.service.FolderService
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.token.service.WfTokenService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class InstanceService(
    private val aliceUserRepository: AliceUserRepository,
    private val userDetailsService: AliceUserDetailsService,
    private val folderService: FolderService,
    private val wfInstanceService: WfInstanceService,
    private val wfTokenService: WfTokenService,
    private val aliceTagService: AliceTagService,
    private val commentService: CommentService
) {
    private val mapper: ObjectMapper =
        ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getInstanceHistory(tokenId: String): List<RestTemplateInstanceHistoryDto>? {
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

    fun getInstanceComments(instanceId: String): List<InstanceCommentDto> {
        val commentsDto = commentService.getInstanceComments(instanceId)
        val moreInfoAddCommentsDto: MutableList<InstanceCommentDto> = mutableListOf()
        commentsDto.forEach {
            val user = aliceUserRepository.getOne(it.createUserKey!!)
            val avatarPath = userDetailsService.makeAvatarPath(user)
            it.avatarPath = avatarPath
            moreInfoAddCommentsDto.add(it)
        }
        return moreInfoAddCommentsDto
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
}
