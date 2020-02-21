package co.brainz.workflow.document.repository

import co.brainz.workflow.document.entity.DocumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository: JpaRepository<DocumentEntity, String> {

    fun findDocumentEntityByDocumentId(documentId: String): DocumentEntity
}
