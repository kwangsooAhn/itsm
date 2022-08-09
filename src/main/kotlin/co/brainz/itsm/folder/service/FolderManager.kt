/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.folder.service

import co.brainz.itsm.folder.constants.FolderConstants
import co.brainz.itsm.folder.entity.WfFolderEntity
import co.brainz.itsm.folder.repository.FolderRepository
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class FolderManager(
    private val folderRepository: FolderRepository
) {

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

    fun findRelatedDocumentListByFolderId(folderId: String): List<RestTemplateRelatedInstanceDto> {
        return folderRepository.findRelatedDocumentListByFolderId(folderId)
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

    fun findFolderOriginByInstanceId(instanceId: String): WfFolderEntity {
        return folderRepository.findFolderOriginByInstanceId(instanceId)
    }

    fun existsOriginFolder(instanceId: String): Boolean {
        return folderRepository.existsByInstance_InstanceIdAndRelatedType(
            instanceId,
            FolderConstants.RelatedType.ORIGIN.code
        )
    }
}
