/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentListDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentRepositoryImpl :
    QuerydslRepositorySupport(RestTemplateDocumentSearchListDto::class.java), WfDocumentRepositoryCustom {

    override fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto):
            MutableList<RestTemplateDocumentListDto> {
        val document = QWfDocumentEntity.wfDocumentEntity
        val query = from(document)
            .select(
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
                    document.updateDt,
                    document.documentIcon
                )
            )
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

        val documentList = mutableListOf<RestTemplateDocumentListDto>()
        for (data in query.results) {
            val document = RestTemplateDocumentListDto(
                documentId = data.documentId,
                documentType = data.documentType,
                documentName = data.documentName,
                documentDesc = data.documentDesc,
                documentStatus = data.documentStatus,
                processId = data.processId,
                formId = data.formId,
                documentNumberingRuleId = data.documentNumberingRuleId,
                documentColor = data.documentColor,
                documentGroup = data.documentGroup,
                createUserKey = data.createUserKey,
                createDt = data.createDt,
                updateUserKey = data.updateUserKey,
                updateDt = data.updateDt,
                totalCount = query.total,
                documentIcon = data.documentIcon
            )
            documentList.add(document)
        }
        return documentList
    }
}
