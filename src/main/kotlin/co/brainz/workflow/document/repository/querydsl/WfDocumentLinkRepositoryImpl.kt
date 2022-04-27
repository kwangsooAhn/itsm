/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentLinkEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions.constant
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentLinkRepositoryImpl :
    QuerydslRepositorySupport(DocumentSearchCondition::class.java), WfDocumentLinkRepositoryCustom {

    override fun findByDocumentLink(documentSearchCondition: DocumentSearchCondition):
            List<DocumentDto> {
        val documentLink = QWfDocumentLinkEntity.wfDocumentLinkEntity

        val query = from(documentLink)
            .select(
                Projections.constructor(
                    DocumentDto::class.java,
                    documentLink.documentLinkId,
                    constant(DocumentConstants.DocumentType.APPLICATION_FORM_LINK.value),
                    documentLink.documentName,
                    documentLink.documentDesc,
                    documentLink.documentStatus,
                    constant(""),
                    constant(""),
                    constant(""),
                    documentLink.documentColor,
                    constant(""),
                    constant(false),
                    documentLink.documentLinkUrl,
                    documentLink.createUserKey,
                    documentLink.createDt,
                    documentLink.updateUserKey,
                    documentLink.updateDt,
                    documentLink.documentIcon
                )
            ).where(
                if (documentSearchCondition.searchDocumentType.equals(DocumentConstants.DocumentType.APPLICATION_FORM.value)) {
                    if (documentSearchCondition.viewType.equals(DocumentConstants.DocumentViewType.ADMIN.value)) {
                        documentLink.documentStatus.`in`(
                            WfDocumentConstants.Status.USE.code,
                            WfDocumentConstants.Status.TEMPORARY.code
                        )
                    } else {
                        super.eq(documentLink.documentStatus, WfDocumentConstants.Status.USE.code)
                    }
                } else {
                    super.eq(documentLink.documentStatus, documentSearchCondition.searchDocumentStatus)
                },
                super.likeIgnoreCase(documentLink.documentName, documentSearchCondition.searchDocuments)
                    ?.or(
                        super.likeIgnoreCase(
                            documentLink.documentDesc,
                            documentSearchCondition.searchDocuments
                        )
                    )
            ).orderBy(documentLink.documentName.asc())

        return query.fetch()
    }

    override fun existsByDocumentLinkName(documentName: String, documentLinkId: String): Boolean {
        val documentLink = QWfDocumentLinkEntity.wfDocumentLinkEntity
        val query = from(documentLink)
            .where(documentLink.documentName.eq(documentName))
        if (documentLinkId.isNotEmpty()) {
            query.where(!documentLink.documentLinkId.eq(documentLinkId))
        }
        return query.fetch().size > 0
    }
}
