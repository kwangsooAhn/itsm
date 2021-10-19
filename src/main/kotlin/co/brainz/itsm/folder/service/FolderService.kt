package co.brainz.itsm.folder.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.folder.constants.FolderConstants
import co.brainz.itsm.folder.dto.FolderDto
import co.brainz.itsm.folder.entity.WfFolderEntity
import co.brainz.itsm.folder.repository.FolderRepository
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
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FolderService(
    private val aliceUserRepository: AliceUserRepository,
    private val userDetailsService: AliceUserDetailsService,
    private val currentSessionUser: CurrentSessionUser,
    private val folderRepository: FolderRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfInstanceRepository: WfInstanceRepository
) {
    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun createFolder(instance: WfInstanceEntity): WfFolderEntity {
        return folderRepository.save(
            WfFolderEntity(
                folderId = UUID.randomUUID().toString().replace("-", ""),
                instance = instance,
                relatedType = FolderConstants.RelatedType.ORIGIN.code,
                createDt = LocalDateTime.now()
            )
        )
    }

    /**
     * [tokenId]의 관련 문서 조회
     */
    fun getRelatedInstance(tokenId: String): List<FolderDto>? {
        val relatedInstances = folderRepository.findRelatedDocumentListByTokenId(tokenId)
        val componentTypeForTopicDisplay = WfComponentConstants.ComponentType.getComponentTypeForTopicDisplay()
        val relatedInstanceList: MutableList<FolderDto> = mutableListOf()
        for (relatedInstance in relatedInstances) {
            val relatedInstanceViewDto = FolderDto(
                folderId = relatedInstance.folderId,
                instanceId = relatedInstance.instanceId,
                relatedType = relatedInstance.relatedType,
                tokenId = relatedInstance.tokenId,
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
                tokenDataList.addAll(wfTokenDataRepository.findTokenDataByTokenIds(tokenIds))
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

        val moreInfoAddRelatedInstance: MutableList<FolderDto> = mutableListOf()
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

    fun insertFolderDto(folderDto: FolderDto): Boolean {
        val wfFolderEntity = WfFolderEntity(
            folderId = folderDto.folderId!!,
            instance = wfInstanceRepository.findByInstanceId(folderDto.instanceId!!)!!,
            relatedType = folderDto.relatedType ?: FolderConstants.RelatedType.REFERENCE.code,
            createUserKey = currentSessionUser.getUserKey(),
            createDt = LocalDateTime.now()
        )
        folderRepository.save(wfFolderEntity)
        return true
    }

    fun insertInstance(originInstance: WfInstanceEntity, relatedInstance: WfInstanceEntity) {
        lateinit var folderId: String
        originInstance.folders?.forEach {
            if (it.relatedType == FolderConstants.RelatedType.ORIGIN.code) {
                folderId = it.folderId
                return@forEach
            }
        }

        folderRepository.save(
            WfFolderEntity(
                folderId = folderId,
                instance = relatedInstance,
                relatedType = FolderConstants.RelatedType.RELATED.code
            )
        )
    }

    fun deleteInstanceInFolder(folderId: String, instanceId: String): Boolean {
        val wfFolderEntity = WfFolderEntity(
            folderId = folderId,
            instance = wfInstanceRepository.findByInstanceId(instanceId)!!
        )
        folderRepository.delete(wfFolderEntity)
        return true
    }
}
