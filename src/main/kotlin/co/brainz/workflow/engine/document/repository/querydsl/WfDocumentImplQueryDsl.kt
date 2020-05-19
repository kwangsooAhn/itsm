package co.brainz.workflow.engine.document.repository.querydsl

import co.brainz.framework.querydsl.AliceQueryDsl
import co.brainz.workflow.engine.document.entity.QWfDocumentEntity
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentImplQueryDsl : QuerydslRepositorySupport(WfDocumentEntity::class.java), WfDocumentQueryDsl {

    override fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto): List<WfDocumentEntity> {
        val document = QWfDocumentEntity.wfDocumentEntity
        val query = from(document)

        query.where(
            super.likeIgnoreCase(document.documentName, searchDto.searchDocuments)
                ?.or(super.likeIgnoreCase(document.documentDesc, searchDto.searchDocuments)),
            document.documentStatus.eq(super.filtered(searchDto.searchDocumentStatus)),
            super.likeIgnoreCase(document.process.processName, searchDto.searchProcessName),
            super.likeIgnoreCase(document.form.formName, super.filtered(searchDto.searchFormName))
        ).orderBy(document.documentName.asc())

        return query.fetch()
    }
}
