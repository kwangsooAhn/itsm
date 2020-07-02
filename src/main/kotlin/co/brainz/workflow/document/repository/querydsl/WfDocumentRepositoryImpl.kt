package co.brainz.workflow.document.repository.querydsl

import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentRepositoryImpl : QuerydslRepositorySupport(RestTemplateDocumentSearchListDto::class.java),
    WfDocumentRepositoryCustom {

    override fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto): List<RestTemplateDocumentDto> {
        val document = QWfDocumentEntity.wfDocumentEntity
        val query = from(document)
            .join(document.process)
            .join(document.form)
            .join(document.numberingRule)
            .where(
                super.eq(document.documentGroup, searchDto.searchGroupName),
                super.eq(document.documentType, searchDto.searchDocumentType),
                if (searchDto.searchDocumentType.equals(DocumentConstants.DocumentType.APPLICATION_FORM.value)) {
                    document.documentStatus.`in`(
                        WfDocumentConstants.Status.TEMPORARY.code,
                        WfDocumentConstants.Status.USE.code
                    )
                } else {
                    super.eq(document.documentStatus, searchDto.searchDocuments)
                },
                super.likeIgnoreCase(document.documentName, searchDto.searchDocuments)
                    ?.or(super.likeIgnoreCase(document.documentDesc, searchDto.searchDocuments)),
                super.likeIgnoreCase(document.process.processName, searchDto.searchProcessName),
                super.likeIgnoreCase(document.form.formName, searchDto.searchFormName)
            ).orderBy(document.documentName.asc())

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
                document.documentGroup,
                document.createUserKey,
                document.createDt,
                document.updateUserKey,
                document.updateDt
            )
        ).fetch()
    }
}
