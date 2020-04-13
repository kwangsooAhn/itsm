package co.brainz.workflow.engine.document.repository

import co.brainz.workflow.engine.document.entity.WfDocumentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WfDocumentDataRepository: JpaRepository<WfDocumentDataEntity, String> {

    fun findByDocumentId(documentId: String): List<WfDocumentDataEntity>

    @Transactional
    fun deleteByDocumentId(documentId: String): Int
}
