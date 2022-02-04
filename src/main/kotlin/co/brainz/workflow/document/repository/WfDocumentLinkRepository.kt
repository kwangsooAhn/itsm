package co.brainz.workflow.document.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.workflow.document.entity.WfDocumentLinkEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfDocumentLinkRepository : JpaRepository<WfDocumentLinkEntity, String>, AliceRepositoryCustom {
    fun findByDocumentLink(documentSearchCondition: DocumentSearchCondition): QueryResults<DocumentDto>
    fun findByDocumentLinkId(documentLinkId: String): WfDocumentLinkEntity
    fun deleteByDocumentLinkId(documentLinkId: String): Int
}
