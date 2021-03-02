package co.brainz.workflow.folder.service

import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.folder.constants.WfFolderConstants
import co.brainz.workflow.folder.entity.WfFolderEntity
import co.brainz.workflow.folder.repository.WfFolderRepository
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceViewDto
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class WfFolderService(
    private val wfFolderRepository: WfFolderRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository
) {
    fun createFolder(instance: WfInstanceEntity): WfFolderEntity {
        return wfFolderRepository.save(
            WfFolderEntity(
                folderId = UUID.randomUUID().toString().replace("-", ""),
                instance = instance,
                relatedType = WfFolderConstants.RelatedType.ORIGIN.code,
                createDt = LocalDateTime.now()
            )
        )
    }

    fun createRelatedFolder(originInstance: WfInstanceEntity, relatedInstance: WfInstanceEntity) {
        lateinit var folderId: String
        originInstance.folders?.forEach {
            if (it.relatedType == WfFolderConstants.RelatedType.ORIGIN.code) {
                folderId = it.folderId
                return@forEach
            }
        }

        wfFolderRepository.save(
            WfFolderEntity(
                folderId = folderId,
                instance = relatedInstance,
                relatedType = WfFolderConstants.RelatedType.RELATED.code
            )
        )
    }

    fun getOriginFolder(tokenId: String): RestTemplateFolderDto {
        val tokenEntity = wfTokenRepository.findById(tokenId)
        val originFolder = tokenEntity.get().instance.folders
        lateinit var restTemplateFolderDto: RestTemplateFolderDto
        originFolder!!.forEach {
            if (it.relatedType == WfFolderConstants.RelatedType.ORIGIN.code) {
                restTemplateFolderDto = RestTemplateFolderDto(
                    folderId = it.folderId,
                    instanceId = it.instance.instanceId,
                    relatedType = it.relatedType,
                    createUserKey = it.createUserKey,
                    createDt = it.createDt,
                    documentNo = null,
                    documentName = null,
                    instanceStartDt = null,
                    instanceEndDt = null,
                    instanceCreateUserKey = null,
                    instanceCreateUserName = null
                )
            }
        }

        return restTemplateFolderDto
    }

    fun getRelatedInstanceList(tokenId: String): List<RestTemplateRelatedInstanceViewDto> {
        val relatedInstances = wfFolderRepository.findRelatedDocumentListByTokenId(tokenId)
        val componentTypeForTopicDisplay = WfComponentConstants.ComponentType.getComponentTypeForTopicDisplay()
        val relatedInstanceViewList: MutableList<RestTemplateRelatedInstanceViewDto> = mutableListOf()
        for (relatedInstance in relatedInstances) {
            val relatedInstanceViewDto = RestTemplateRelatedInstanceViewDto(
                folderId = relatedInstance.folderId,
                instanceId = relatedInstance.instanceId,
                relatedType = relatedInstance.relatedType,
                tokenId = relatedInstance.tokenId,
                documentNo = relatedInstance.documentNo,
                documentName = relatedInstance.documentName,
                documentColor = relatedInstance.documentColor,
                createUserKey = relatedInstance.createUserKey,
                createDt = relatedInstance.createDt,
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
            relatedInstanceViewList.add(relatedInstanceViewDto)
        }
        return relatedInstanceViewList
    }

    fun createFolderData(restTemplateFolderDtoList: List<RestTemplateFolderDto>): Boolean {
        restTemplateFolderDtoList.forEach {
            val wfFolderEntity = WfFolderEntity(
                folderId = it.folderId!!,
                instance = wfInstanceRepository.findByInstanceId(it.instanceId)!!,
                relatedType = WfFolderConstants.RelatedType.REFERENCE.code,
                createUserKey = it.createUserKey,
                createDt = LocalDateTime.now()
            )

            wfFolderRepository.save(wfFolderEntity)
        }
        return true
    }

    fun deleteFolderData(folderId: String, restTemplateFolderDto: RestTemplateFolderDto): Boolean {
        val wfFolderEntity = WfFolderEntity(
            folderId = folderId,
            instance = wfInstanceRepository.findByInstanceId(restTemplateFolderDto.instanceId)!!
        )
        wfFolderRepository.delete(wfFolderEntity)
        return true
    }
}
