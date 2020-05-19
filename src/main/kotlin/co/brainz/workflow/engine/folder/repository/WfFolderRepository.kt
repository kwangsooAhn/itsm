package co.brainz.workflow.engine.folder.repository

import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.engine.folder.entity.WfFolderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfFolderRepository : JpaRepository<WfFolderEntity, String> {
    @Query(
        "SELECT NEW co.brainz.workflow.provider.dto.RestTemplateFolderDto(" +
                "f.instance.instanceId, i.document.documentName, i.instanceStartDt, i.instanceEndDt, i.instanceCreateUser.userKey, i.instanceCreateUser.userName) FROM WfFolderEntity f, WfInstanceEntity i " +
                "WHERE f.folderId = (SELECT sf.folderId FROM WfFolderEntity sf, WfTokenEntity st " +
                "                     WHERE st.tokenId = :tokenId " +
                "                       AND st.instance = sf.instance) " +
                "AND f.instance = i " +
                "ORDER BY i.instanceStartDt"
    )
    fun findRelatedDocumentListByTokenId(tokenId: String): List<RestTemplateFolderDto>
}
