/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.folder.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.folder.constants.FolderConstants
import co.brainz.itsm.folder.dto.FolderMappingDto
import co.brainz.itsm.folder.dto.InstanceFolderListDto
import co.brainz.itsm.folder.dto.InstanceInFolderDto
import co.brainz.itsm.folder.entity.WfFolderEntity
import co.brainz.itsm.folder.repository.FolderRepository
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FolderService(
    private val aliceUserRepository: AliceUserRepository,
    private val userDetailsService: AliceUserDetailsService,
    private val currentSessionUser: CurrentSessionUser,
    private val folderRepository: FolderRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val folderManager: FolderManager,
    private val instanceService: InstanceService
) {
    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun createFolder(instance: WfInstanceEntity): WfFolderEntity {
        return folderManager.createFolder(instance)
    }

    /**
     * [folderId]의 관련 문서 조회
     */
    fun getFolder(folderId: String): List<InstanceInFolderDto>? {
        val relatedInstances = folderManager.findRelatedDocumentListByFolderId(folderId)
        val componentTypeForTopicDisplay = WfComponentConstants.ComponentType.getComponentTypeForTopicDisplay()
        val relatedInstanceList: MutableList<InstanceInFolderDto> = mutableListOf()
        for (relatedInstance in relatedInstances) {
            val relatedInstanceViewDto = InstanceInFolderDto(
                folderId = relatedInstance.folderId,
                instanceId = relatedInstance.instanceId,
                relatedType = relatedInstance.relatedType,
                tokenId = relatedInstance.tokenId,
                documentId = relatedInstance.documentId,
                documentNo = relatedInstance.documentNo,
                documentName = relatedInstance.documentName,
                documentColor = relatedInstance.documentColor,
                instanceStartDt = relatedInstance.instanceStartDt,
                instanceEndDt = relatedInstance.instanceEndDt,
                instanceStatus = relatedInstance.instanceStatus,
                instanceCreateUserKey = relatedInstance.instanceCreateUserKey,
                instanceCreateUserName = relatedInstance.instanceCreateUserName,
                avatarPath = relatedInstance.avatarPath
            )
            val tokenIds = mutableSetOf<String>()
            relatedInstance.tokenId?.let { tokenIds.add(it) }
            val tokenDataList = mutableListOf<WfInstanceListTokenDataDto>()
            if (tokenIds.isNotEmpty()) {
                tokenDataList.addAll(wfTokenDataRepository.findTokenDataByTokenIds(tokenIds, componentTypeForTopicDisplay))
            }
            val topics = mutableListOf<String>()
            tokenDataList.forEach { tokenData ->
                if (tokenData.component.isTopic &&
                    componentTypeForTopicDisplay.indexOf(tokenData.component.componentType) > -1
                ) {
                    topics.add(tokenData.value)
                }
            }
            if (topics.isNotEmpty()) {
                relatedInstanceViewDto.topics = topics
            }
            relatedInstanceList.add(relatedInstanceViewDto)
        }

        val moreInfoAddRelatedInstance: MutableList<InstanceInFolderDto> = mutableListOf()
        relatedInstanceList.forEach {
            val user = aliceUserRepository.getOne(it.instanceCreateUserKey!!)
            val avatarPath = userDetailsService.makeAvatarPath(user)
            it.avatarPath = avatarPath
            moreInfoAddRelatedInstance.add(it)
        }
        return moreInfoAddRelatedInstance
    }

    fun getFolderId(tokenId: String): String? {
        val tokenEntity = wfTokenRepository.findById(tokenId)
        val originFolder = tokenEntity.get().instance.folders
        lateinit var folderId: String
        originFolder!!.forEach {
            if (it.relatedType == FolderConstants.RelatedType.ORIGIN.code) {
                folderId = it.folderId
            }
        }

        return folderId
    }

    @Transactional
    fun insertFolderDto(instanceFolderListDto: InstanceFolderListDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var isSuccess = true
        var orgFolderId = ""
        if (instanceFolderListDto.instanceId.isNotEmpty()) {
            isSuccess = instanceService.setInitInstance(instanceFolderListDto.instanceId, instanceFolderListDto.documentId)
            orgFolderId = folderManager.findFolderOriginByInstanceId(instanceFolderListDto.instanceId).folderId
            instanceFolderListDto.folders.forEach {
                it.folderId = folderManager.findFolderOriginByInstanceId(it.instanceId!!).folderId
            }
        }
        if (isSuccess) {
            val folderEntityList = mutableListOf<WfFolderEntity>()
            instanceFolderListDto.folders.forEach {
                // 현재 문서 > 연관 문서 정보
                folderEntityList.add(
                    WfFolderEntity(
                        folderId = orgFolderId,
                        instance = wfInstanceRepository.findByInstanceId(it.instanceId!!)!!,
                        relatedType = it.relatedType ?: FolderConstants.RelatedType.REFERENCE.code,
                        createUserKey = currentSessionUser.getUserKey(),
                        createDt = LocalDateTime.now()
                    )
                )
                // 연관 문서 > 현재 문서 정보
                folderEntityList.add(
                    WfFolderEntity(
                        folderId = it.folderId!!,
                        instance = wfInstanceRepository.findByInstanceId(instanceFolderListDto.instanceId)!!,
                        relatedType = it.relatedType ?: FolderConstants.RelatedType.REFERENCE.code,
                        createUserKey = currentSessionUser.getUserKey(),
                        createDt = LocalDateTime.now()
                    )
                )
            }
            folderRepository.saveAll(folderEntityList)
        }

        if (!isSuccess) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        val dataMap = mutableMapOf<String, String>()
        dataMap["folderId"] = orgFolderId

        return ZResponse(
            status = status.code,
            data = dataMap
        )
    }

    fun insertInstance(originInstance: WfInstanceEntity, relatedInstance: WfInstanceEntity) {
        return folderManager.insertInstance(originInstance, relatedInstance)
    }

    fun deleteInstanceInFolder(folderMappingDto: FolderMappingDto): ZResponse {
        val folderEntityList = mutableListOf<WfFolderEntity>()
        // 현재 문서 > 연관 문서 정보
        folderEntityList.add(
            WfFolderEntity(
                folderId = folderMappingDto.folderId,
                instance = wfInstanceRepository.findByInstanceId(folderMappingDto.relInstanceId)!!
            )
        )
        // 연관 문서 > 현재 문서 정보
        folderEntityList.add(
            WfFolderEntity(
                folderId = folderManager.findFolderOriginByInstanceId(folderMappingDto.relInstanceId).folderId,
                instance = wfInstanceRepository.findByInstanceId(folderMappingDto.orgInstanceId)!!
            )
        )

        folderRepository.deleteAll(folderEntityList)

        return ZResponse()
    }
}
