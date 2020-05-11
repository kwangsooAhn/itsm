package co.brainz.workflow.engine.folder.service

import co.brainz.workflow.engine.folder.constants.WfFolderConstants
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.engine.folder.entity.WfFolderEntity
import co.brainz.workflow.engine.folder.repository.WfFolderRepository
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class WfFolderService(
    private val wfFolderRepository: WfFolderRepository
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

    fun getRelatedInstanceList(tokenId: String): List<RestTemplateFolderDto> {
        return wfFolderRepository.findRelatedDocumentListByTokenId(tokenId)
    }
}
