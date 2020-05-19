package co.brainz.workflow.engine.document.repository

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.querydsl.WfDocumentQueryDsl
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WfDocumentRepository : JpaRepository<WfDocumentEntity, String>, WfDocumentQueryDsl {

    fun findDocumentEntityByDocumentId(documentId: String): WfDocumentEntity
    fun findByDocumentId(documentId: String): WfDocumentEntity?

    @Transactional
    fun deleteByDocumentId(documentId: String): Int

    fun findByFormAndProcess(wfFormEntity: WfFormEntity, wfProcessEntity: WfProcessEntity): WfDocumentEntity?
}
