package co.brainz.workflow.document.repository

import co.brainz.workflow.document.entity.WfDocumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfDocumentRepository: JpaRepository<WfDocumentEntity, String> {

    fun findDocumentEntityByDocumentId(documentId: String): WfDocumentEntity
}
