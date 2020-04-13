package co.brainz.workflow.engine.document.repository

import co.brainz.workflow.engine.document.entity.WfDocumentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfDocumentDataRepository: JpaRepository<WfDocumentDataEntity, String> {

    fun findByDocumentId(documentId: String): List<WfDocumentDataEntity>

}
