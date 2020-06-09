package co.brainz.workflow.folder.repository

import co.brainz.workflow.provider.dto.RestTemplateFolderDto

interface WfFolderRepositoryCustom {

    fun findRelatedDocumentListByTokenId(tokenId: String): List<RestTemplateFolderDto>
}
