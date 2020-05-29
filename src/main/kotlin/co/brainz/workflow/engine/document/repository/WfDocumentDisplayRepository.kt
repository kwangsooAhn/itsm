package co.brainz.workflow.engine.document.repository

import co.brainz.workflow.engine.document.entity.WfDocumentDisplayEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WfDocumentDisplayRepository : JpaRepository<WfDocumentDisplayEntity, String> {

    @Transactional
    fun deleteByDocumentId(documentId: String): Int

    @Query(
        "SELECT dd " +
                "FROM WfDocumentDisplayEntity dd " +
                "WHERE dd.documentId = :documentId " +
                "ORDER BY dd.componentId, dd.elementId DESC"
    )
    fun findByDocumentId(documentId: String): List<WfDocumentDisplayEntity>

    fun findByDocumentIdAndElementId(documentId: String, elementId: String): List<WfDocumentDisplayEntity>
}
