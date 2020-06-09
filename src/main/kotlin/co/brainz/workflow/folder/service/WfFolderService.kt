package co.brainz.workflow.folder.service

import co.brainz.workflow.folder.constants.WfFolderConstants
import co.brainz.workflow.folder.entity.WfFolderEntity
import co.brainz.workflow.folder.repository.WfFolderRepository
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class WfFolderService(
    private val wfFolderRepository: WfFolderRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository
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

    fun getRelatedInstanceList(tokenId: String): List<RestTemplateFolderDto> {
        return wfFolderRepository.findRelatedDocumentListByTokenId(tokenId)
    }

    fun createFolderData(restTemplateFolderDto: List<RestTemplateFolderDto>) {
        restTemplateFolderDto.forEach {
            val wfFolderEntity = WfFolderEntity(
                folderId = it.folderId!!,
                instance = wfInstanceRepository.findByInstanceId(it.instanceId)!!,
                relatedType = WfFolderConstants.RelatedType.REFERENCE.code,
                createUserKey = it.createUserKey,
                createDt = LocalDateTime.now()
            )

            wfFolderRepository.save(wfFolderEntity)
        }
    }

    fun deleteFolderData(folderId: String, restTemplateFolderDto: RestTemplateFolderDto) {
        val wfFolderEntity = WfFolderEntity(
            folderId = folderId,
            instance = wfInstanceRepository.findByInstanceId(restTemplateFolderDto.instanceId)!!
        )

        wfFolderRepository.delete(wfFolderEntity)
    }
}
