package co.brainz.workflow.engine.document.repository.querydsl

import co.brainz.workflow.engine.document.entity.QWfDocumentEntity
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentRepositoryImpl : QuerydslRepositorySupport(WfDocumentEntity::class.java), WfDocumentRepositoryCustom {

    override fun getDocumentsssss(searchDto: RestTemplateDocumentSearchListDto): List<WfDocumentEntity> {
        val document = QWfDocumentEntity.wfDocumentEntity
        val query = from(document)

        query.where(
            super.likeIgnoreCase(document.documentName, searchDto.searchDocuments)
                ?.or(super.likeIgnoreCase(document.documentDesc, searchDto.searchDocuments)),
            super.eq(document.documentStatus, searchDto.searchDocumentStatus),
            super.likeIgnoreCase(document.process.processName, searchDto.searchProcessName),
            super.likeIgnoreCase(document.form.formName, searchDto.searchFormName)
        )

        return query.fetch()
    }
}
