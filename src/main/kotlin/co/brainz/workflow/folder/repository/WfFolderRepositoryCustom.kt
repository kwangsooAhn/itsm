package co.brainz.workflow.folder.repository

import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto

interface WfFolderRepositoryCustom {
    fun findRelatedDocumentListByTokenId(tokenId: String): List<RestTemplateRelatedInstanceDto>
}
