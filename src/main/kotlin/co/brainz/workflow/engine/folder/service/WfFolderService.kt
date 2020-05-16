package co.brainz.workflow.engine.folder.service

import co.brainz.workflow.engine.folder.constants.WfFolderConstants
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.engine.folder.entity.WfFolderEntity
import co.brainz.workflow.engine.folder.repository.WfFolderRepository
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import java.util.UUID
import org.springframework.stereotype.Service


@Service
class WfFolderService(
    private val wfFolderRepository: WfFolderRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository
) {
    fun createFolder(instance: WfInstanceEntity) {
        wfFolderRepository.save(
            WfFolderEntity(
                folderId = UUID.randomUUID().toString().replace("-", ""),
                instance = instance,
                relatedType = WfFolderConstants.RelatedType.ORIGIN.code
            )
        )
    }

    fun addInstance(originInstance: WfInstanceEntity, addedInstance: WfInstanceEntity) {
        lateinit var folderId: String
        originInstance.folders?.forEach {
            if (it.relatedType == WfFolderConstants.RelatedType.ORIGIN.code) {
                folderId = it.folderId
            }
        }

        wfFolderRepository.save(
            WfFolderEntity(
                folderId = folderId,
                instance = addedInstance,
                relatedType = WfFolderConstants.RelatedType.ORIGIN.code
            )
        )
    }

    fun getOriginFolder(tokenId: String): RestTemplateFolderDto {
        val tokenEntity = wfTokenRepository.findById(tokenId)
        var originFolder = tokenEntity.get().instance.folders
        var restTemplateFolderDto: RestTemplateFolderDto? = null;
        originFolder!!.forEach {
            if (it.relatedType == WfFolderConstants.RelatedType.ORIGIN.code) {
                    restTemplateFolderDto = RestTemplateFolderDto(
                    folderId = it.folderId,
                    instanceId = it.instance.instanceId,
                    relatedType = it.relatedType,
                    documentName = null,
                    instanceStartDt = null,
                    instanceEndDt = null,
                    instanceCreateUserKey = null,
                    instanceCreateUserName = null
                )
            }
        }

        return restTemplateFolderDto!!
    }

    fun getRelatedInstanceList(tokenId: String): List<RestTemplateFolderDto> {
        return wfFolderRepository.findRelatedDocumentListByTokenId(tokenId)
    }

    fun createFolderData(restTemplateFolderDto: List<RestTemplateFolderDto>) {
        restTemplateFolderDto.forEach { restTemplateFolderDto ->
            var wfFolderEntity = WfFolderEntity (
                folderId = restTemplateFolderDto.folderId!!,
                instance = wfInstanceRepository.findByInstanceId(restTemplateFolderDto.instanceId!!)!!,
                relatedType = WfFolderConstants.RelatedType.REFERENCE.code
            )

            wfFolderRepository.save(wfFolderEntity)
        }
    }

    fun deleteFolderData(restTemplateFolderDto: RestTemplateFolderDto) {
        var wfFolderEntity = WfFolderEntity (
            folderId = restTemplateFolderDto.folderId!!,
            instance = wfInstanceRepository.findByInstanceId(restTemplateFolderDto.instanceId!!)!!
        )

        wfFolderRepository.delete(wfFolderEntity)
    }
}
