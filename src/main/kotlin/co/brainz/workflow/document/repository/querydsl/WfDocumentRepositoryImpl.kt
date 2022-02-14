/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions.constant
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentRepositoryImpl :
    QuerydslRepositorySupport(DocumentSearchCondition::class.java), WfDocumentRepositoryCustom {

    override fun findByDocuments(documentSearchCondition: DocumentSearchCondition):
            QueryResults<DocumentDto> {
        val document = QWfDocumentEntity.wfDocumentEntity

        val documentQuery = from(document)
            .select(
                Projections.constructor(
                    DocumentDto::class.java,
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
                    document.apiEnable,
                    constant(""),
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
                super.eq(document.documentGroup, documentSearchCondition.searchGroupName),
                super.eq(document.documentType, documentSearchCondition.searchDocumentType),
                if (documentSearchCondition.searchDocumentType.equals(DocumentConstants.DocumentType.APPLICATION_FORM.value)) {
                    if (documentSearchCondition.viewType.equals(DocumentConstants.DocumentViewType.ADMIN.value)) {
                        document.documentStatus.`in`(
                            WfDocumentConstants.Status.USE.code,
                            WfDocumentConstants.Status.TEMPORARY.code
                        )
                    } else {
                        super.eq(document.documentStatus, WfDocumentConstants.Status.USE.code)
                    }
                } else {
                    super.eq(document.documentStatus, documentSearchCondition.searchDocumentStatus)
                },
                super.likeIgnoreCase(document.documentName, documentSearchCondition.searchDocuments)
                    ?.or(
                        super.likeIgnoreCase(
                            document.documentDesc,
                            documentSearchCondition.searchDocuments
                        )
                    ),
                super.likeIgnoreCase(
                    document.process.processName,
                    documentSearchCondition.searchProcessName
                ),
                super.likeIgnoreCase(document.form.formName, documentSearchCondition.searchFormName)
            ).orderBy(document.documentName.asc())

        return documentQuery.fetchResults()
    }

    override fun findAllByDocuments(documentSearchCondition: DocumentSearchCondition):
            MutableList<DocumentDto> {
        val document = QWfDocumentEntity.wfDocumentEntity
        val documentQuery = from(document)
            .select(
                Projections.constructor(
                    DocumentDto::class.java,
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
                    document.apiEnable,
                    constant(""),
                    document.createUserKey,
                    document.createDt,
                    document.updateUserKey,
                    document.updateDt,
                    document.documentIcon
                )
            )
            .where(
                if (documentSearchCondition.viewType.equals(DocumentConstants.DocumentViewType.ADMIN.value)) {
                    document.documentStatus.`in`(
                        WfDocumentConstants.Status.USE.code,
                        WfDocumentConstants.Status.TEMPORARY.code,
                        WfDocumentConstants.Status.DESTROY.code
                    )
                } else {
                    document.documentStatus.`in`(
                        WfDocumentConstants.Status.USE.code,
                        WfDocumentConstants.Status.TEMPORARY.code
                    )
                }
            ).orderBy(document.documentName.asc())
            .fetchResults()
        return documentQuery.results
    }

    override fun getDocumentListByNumberingId(numberingId: String): List<WfDocumentEntity> {
        val document = QWfDocumentEntity.wfDocumentEntity
        return from(document)
            .where(document.numberingRule.numberingId.eq(numberingId))
            .fetch()
    }

    override fun existsByDocumentName(documentName: String, documentId: String): Boolean {
        val documentEntity = QWfDocumentEntity.wfDocumentEntity
        val query = from(documentEntity)
            .where(documentEntity.documentName.eq(documentName))
        if (documentId.isNotEmpty()) {
            query.where(!documentEntity.documentId.eq(documentId))
        }
        return query.fetchCount() > 0
    }
}
