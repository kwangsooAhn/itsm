package co.brainz.workflow.engine.document.repository.querydsl

import co.brainz.workflow.engine.document.entity.QWfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentRepositoryImpl : QuerydslRepositorySupport(RestTemplateDocumentSearchListDto::class.java),
    WfDocumentRepositoryCustom {

    override fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto): List<RestTemplateDocumentDto> {
        val document = QWfDocumentEntity.wfDocumentEntity

        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(document.documentName, searchDto.searchDocuments)
                ?.or(super.likeIgnoreCase(document.documentDesc, searchDto.searchDocuments))
        )
        builder.and(super.likeIgnoreCase(document.process.processName, searchDto.searchProcessName))
        builder.and(super.likeIgnoreCase(document.form.formName, searchDto.searchFormName))
        if (searchDto.searchDocumentType?.isNotEmpty()!!) {
            builder.and(document.documentType.eq(searchDto.searchDocumentType))
        }
        if (searchDto.searchDocumentStatus?.isNotEmpty()!!) {
            builder.and(document.documentStatus.eq(searchDto.searchDocumentStatus))
        }

        val query = from(document)
            .join(document.process)
            .join(document.form)
            .join(document.numberingRule)
            .where(builder).orderBy(document.documentName.asc())

        return query.select(
            Projections.constructor(
                RestTemplateDocumentDto::class.java,
                document.documentId,
                document.documentType,
                document.documentName,
                document.documentDesc,
                document.documentStatus,
                document.process.processId,
                document.form.formId,
                document.numberingRule.numberingId,
                document.documentColor,
                document.createUserKey,
                document.createDt,
                document.updateUserKey,
                document.updateDt
            )
        ).fetch()
    }
}
