/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.form.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.form.entity.QWfFormEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.provider.constants.WorkflowConstants
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.CaseBuilder
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfFormRepositoryImpl : QuerydslRepositorySupport(WfFormEntity::class.java),
    WfFormRepositoryCustom {

    override fun findFormEntityList(formSearchCondition: FormSearchCondition): PagingReturnDto {
        val form = QWfFormEntity.wfFormEntity
        val query = from(form)
            .innerJoin(form.createUser).fetchJoin()
            .leftJoin(form.updateUser).fetchJoin()
            .where(builder(formSearchCondition, form))
        if (formSearchCondition.statusArray?.isNotEmpty() == true) {
            query.orderBy(form.formName.asc())
        } else {
            val statusNumber = CaseBuilder()
                .`when`(form.formStatus.eq(WorkflowConstants.FormStatus.EDIT.value)).then(1)
                .`when`(form.formStatus.eq(WorkflowConstants.FormStatus.PUBLISH.value)).then(2)
                .`when`(form.formStatus.eq(WorkflowConstants.FormStatus.USE.value)).then(3)
                .`when`(form.formStatus.eq(WorkflowConstants.FormStatus.DESTROY.value)).then(4)
                .otherwise(5)
            query.orderBy(statusNumber.asc())
                .orderBy(form.updateDt.coalesce(form.createDt).desc())
        }
        if (formSearchCondition.isPaging) {
            query.limit(formSearchCondition.contentNumPerPage)
            query.offset((formSearchCondition.pageNum - 1) * formSearchCondition.contentNumPerPage)
        }

        val countQuery = from(form)
            .select(form.count())
            .where(builder(formSearchCondition, form))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    override fun findFormDocumentExist(formId: String): Boolean {
        val form = QWfFormEntity.wfFormEntity
        val document = QWfDocumentEntity.wfDocumentEntity
        return from(form)
            .innerJoin(document).on(document.form.formId.eq(form.formId)).fetchJoin()
            .where(
                form.formId.eq(formId)
                    .and(
                        document.documentStatus.`in`(
                            WfDocumentConstants.Status.USE.code,
                            WfDocumentConstants.Status.TEMPORARY.code
                        )
                    )
            ).fetch().size > 0
    }
    private fun builder(formSearchCondition: FormSearchCondition, form: QWfFormEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        if (formSearchCondition.searchValue?.isNotEmpty() == true) {
            builder.and(
                form.formName.containsIgnoreCase(formSearchCondition.searchValue.trim())
                    .or(form.formDesc.containsIgnoreCase(formSearchCondition.searchValue.trim()))
            )
        }
        if (formSearchCondition.statusArray?.isNotEmpty() == true) {
            builder.and(form.formStatus.`in`(formSearchCondition.statusArray))
        }
        return builder
    }
}
