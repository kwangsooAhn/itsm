package co.brainz.workflow.engine.document.repository

import co.brainz.workflow.engine.document.entity.WfDocumentDataEntity
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WfDocumentDataRepository: JpaRepository<WfDocumentDataEntity, String> {

    @Transactional
    fun deleteByDocumentId(documentId: String): Int

    fun findByDocument(document: WfDocumentEntity): List<WfDocumentDataEntity>
}
