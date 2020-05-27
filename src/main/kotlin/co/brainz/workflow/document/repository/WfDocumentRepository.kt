package co.brainz.workflow.document.repository

import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.process.entity.WfProcessEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WfDocumentRepository : JpaRepository<WfDocumentEntity, String>, JpaSpecificationExecutor<WfDocumentEntity> {

    fun findDocumentEntityByDocumentId(documentId: String): WfDocumentEntity
    fun findByDocumentId(documentId: String): WfDocumentEntity?

    @Transactional
    fun deleteByDocumentId(documentId: String): Int

    fun findByFormAndProcess(wfFormEntity: WfFormEntity, wfProcessEntity: WfProcessEntity): WfDocumentEntity?

    fun findByDocumentStatusOrderByDocumentName(documentStatus: List<String>): List<WfDocumentEntity>
}
