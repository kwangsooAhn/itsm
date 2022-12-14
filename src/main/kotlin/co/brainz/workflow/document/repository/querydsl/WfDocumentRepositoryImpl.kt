/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.document.dto.FieldOptionDto
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions.constant
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfDocumentRepositoryImpl :
    QuerydslRepositorySupport(DocumentSearchCondition::class.java), WfDocumentRepositoryCustom {

    override fun findByDocuments(documentSearchCondition: DocumentSearchCondition, targetIds: List<String>?):
        List<DocumentDto> {
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
                    document.updateDt
                )
            )
            .join(document.process)
            .join(document.form)
            .join(document.numberingRule)
            .where(
                super.eq(document.documentGroup, documentSearchCondition.searchGroupName),
                if (documentSearchCondition.searchDocumentType == WfDocumentConstants.DocumentViewType.SUBPROCESS.value) {
                    // processDesigner > search subprocesses
                    document.documentType.`in`(
                        DocumentConstants.DocumentType.WORKFLOW.value,
                        DocumentConstants.DocumentType.APPLICATION_FORM_WORKFLOW.value
                    )
                } else {
                    // search documents
                    document.documentType.`in`(
                        DocumentConstants.DocumentType.APPLICATION_FORM.value,
                        DocumentConstants.DocumentType.APPLICATION_FORM_WORKFLOW.value,
                        DocumentConstants.DocumentType.WORKFLOW.value
                    )
                },
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
            )
            if (targetIds!!.isNotEmpty()) {
                documentQuery.where(document.documentId.`in`(targetIds))
            }
            documentQuery.orderBy(document.documentName.asc())

        return documentQuery.fetch()
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
                    document.updateDt
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
        return documentQuery.fetch()
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
        return query.fetch().size > 0
    }

    override fun getDocumentListByIds(documentIds: Set<String>): List<WfDocumentEntity> {
        val document = QWfDocumentEntity.wfDocumentEntity
        return from(document)
            .where(document.documentId.`in`(documentIds))
            .fetch()
    }

    override fun getSearchFieldValues(fieldOptionDto: FieldOptionDto): List<Array<Any>> {
        val sqlBuilder = StringBuilder()
        sqlBuilder.append("select ")
        fieldOptionDto.fields.forEachIndexed { index, field ->
            if (index > 0) {
                sqlBuilder.append(", ")
            }
            sqlBuilder.append(field.name)
        }
        sqlBuilder.append(" from " + fieldOptionDto.table)
        sqlBuilder.append(" where ${fieldOptionDto.keyField} = '${fieldOptionDto.documentNo}'")
        if (fieldOptionDto.sort.field.isNotEmpty()) {
            sqlBuilder.append(" order by ${fieldOptionDto.sort.field} ${fieldOptionDto.sort.order}")
        } else {
            sqlBuilder.append(" order by 1 ${fieldOptionDto.sort.order}")
        }
        sqlBuilder.append(" limit ${WfComponentConstants.LIST_LIMIT}")
        return entityManager?.createNativeQuery(sqlBuilder.toString())?.resultList as List<Array<Any>>
    }

    override fun existsByFormId(formId: String): Boolean {
        val document = QWfDocumentEntity.wfDocumentEntity
        return from(document)
            .where(document.form.formId.eq(formId))
            .fetchFirst() != null
    }

    override fun existsByProcessId(processId: String): Boolean {
        val document = QWfDocumentEntity.wfDocumentEntity
        return from(document)
            .where(document.process.processId.eq(processId))
            .fetchFirst() != null
    }
}
