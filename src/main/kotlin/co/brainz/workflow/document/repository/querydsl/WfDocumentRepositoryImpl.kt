package co.brainz.workflow.document.repository.querydsl

import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentRepositoryImpl :
    QuerydslRepositorySupport(RestTemplateDocumentSearchListDto::class.java),
    WfDocumentRepositoryCustom {

    override fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto): QueryResults<WfDocumentEntity> {
        val document = QWfDocumentEntity.wfDocumentEntity
        return from(document)
            .join(document.process)
            .join(document.form)
            .join(document.numberingRule)
            .where(
                super.eq(document.documentGroup, searchDto.searchGroupName),
                super.eq(document.documentType, searchDto.searchDocumentType),
                if (searchDto.searchDocumentType.equals(DocumentConstants.DocumentType.APPLICATION_FORM.value)) {
                    if (searchDto.viewType.equals(DocumentConstants.DocumentViewType.ADMIN.value)) {
                        document.documentStatus.`in`(
                            WfDocumentConstants.Status.USE.code,
                            WfDocumentConstants.Status.TEMPORARY.code
                        )
                    } else {
                        super.eq(document.documentStatus, WfDocumentConstants.Status.USE.code)
                    }
                } else {
                    super.eq(document.documentStatus, searchDto.searchDocumentStatus)
                },
                super.likeIgnoreCase(document.documentName, searchDto.searchDocuments?.trim())
                    ?.or(
                        super.likeIgnoreCase(
                            document.documentDesc,
                            searchDto.searchDocuments?.trim()
                        )
                    ),
                super.likeIgnoreCase(
                    document.process.processName,
                    searchDto.searchProcessName?.trim()
                ),
                super.likeIgnoreCase(document.form.formName, searchDto.searchFormName?.trim())
            ).orderBy(document.documentName.asc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(searchDto.offset)
            .fetchResults()
    }
}
