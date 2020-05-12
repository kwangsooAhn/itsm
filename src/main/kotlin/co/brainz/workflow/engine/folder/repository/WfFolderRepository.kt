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
                "f.instance.instanceId, i.document.documentName, i.instanceStartDt, i.instanceEndDt, i.instanceCreateUserKey, i.instanceCreateUserKey) FROM WfFolderEntity f, WfInstanceEntity i " +
                "WHERE f.folderId = (SELECT sf.folderId FROM WfFolderEntity sf, WfTokenEntity st " +
                "                     WHERE st.tokenId = :tokenId " +
                "                       AND st.instance = sf.instance) " +
                "AND f.instance = i " +
                "AND (lower(i.document.documentName) like lower(concat('%', :searchValue, '%')) " +
                "or lower(i.instanceCreateUserKey) like lower(concat('%', :searchValue, '%')) " +
                "or :searchValue is null or :searchValue = '') " +
                "ORDER BY i.instanceStartDt"
    )
    fun findRelatedDocumentListByTokenId(tokenId: String, searchValue: String): List<RestTemplateFolderDto>
}
