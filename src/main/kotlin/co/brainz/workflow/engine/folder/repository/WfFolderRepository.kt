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
                "f.folderId, f.instance.instanceId, f.relatedType, i.document.documentName, f.createUserKey, f.createDt, i.instanceStartDt, i.instanceEndDt, i.instanceCreateUserKey, i.aliceUserEntity.userName) FROM WfFolderEntity f, WfInstanceEntity i left join i.aliceUserEntity on i.instanceCreateUserKey = i.aliceUserEntity.userKey " +
                "WHERE f.folderId = (SELECT sf.folderId FROM WfFolderEntity sf, WfTokenEntity st " +
                "                     WHERE st.tokenId = :tokenId " +
                "                       AND st.instance = sf.instance) " +
                "AND f.instance = i " +
                "ORDER BY i.instanceStartDt"
    )
    fun findRelatedDocumentListByTokenId(tokenId: String): List<RestTemplateFolderDto>
}
