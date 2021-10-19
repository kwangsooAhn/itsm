package co.brainz.itsm.folder.repository

import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto

interface FolderRepositoryCustom {
    fun findRelatedDocumentListByTokenId(tokenId: String): List<RestTemplateRelatedInstanceDto>
}
