package co.brainz.itsm.folder.repository

import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto

interface FolderRepositoryCustom {
    fun findRelatedDocumentListByFolderId(folderId: String): List<RestTemplateRelatedInstanceDto>
    fun findFolderIdByTokenId(tokenId: String): String
}
