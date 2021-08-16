/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.form.repository

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.form.entity.QWfFormEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.ProcessListReturnDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateFormListReturnDto
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfFormRepositoryImpl : QuerydslRepositorySupport(WfFormEntity::class.java),
    WfFormRepositoryCustom {

    override fun findFormEntityList(formSearchCondition: FormSearchCondition): RestTemplateFormListReturnDto {
        val form = QWfFormEntity.wfFormEntity
        val query = from(form)
            .select(
                Projections.constructor(
                    RestTemplateFormDto::class.java,
                    form.formId,
                    form.formName,
                    form.formStatus,
                    form.formDesc,
                    form.formDisplayOption,
                    form.formCategory,
                    null, // editable
                    form.createUser.userKey,
                    form.createUser.userName,
                    form.createDt,
                    form.updateUser.userKey,
                    form.updateUser.userName,
                    form.updateDt
                )
            )
            .innerJoin(form.createUser).fetchJoin()
            .leftJoin(form.updateUser).fetchJoin()
        if (formSearchCondition.searchValue?.isNotEmpty() == true) {
            query.where(
                form.formName.contains(formSearchCondition.searchValue)
                    .or(form.formDesc.contains(formSearchCondition.searchValue))
            )
        }
        if (formSearchCondition.statusArray?.isNotEmpty() == true) {
            query.where(form.formStatus.`in`(formSearchCondition.statusArray)).orderBy(form.formName.asc())
        } else {
            val statusNumber = CaseBuilder()
                .`when`(form.formStatus.eq(RestTemplateConstants.FormStatus.EDIT.value)).then(1)
                .`when`(form.formStatus.eq(RestTemplateConstants.FormStatus.PUBLISH.value)).then(2)
                .`when`(form.formStatus.eq(RestTemplateConstants.FormStatus.USE.value)).then(3)
                .`when`(form.formStatus.eq(RestTemplateConstants.FormStatus.DESTROY.value)).then(4)
                .otherwise(5)
            query.orderBy(statusNumber.asc())
                .orderBy(form.updateDt.coalesce(form.createDt).desc())
        }
        query.limit(formSearchCondition.contentNumPerPage)
        query.offset((formSearchCondition.pageNum - 1) * formSearchCondition.contentNumPerPage)

        val queryResult = query.fetchResults()
        return RestTemplateFormListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    override fun findFormDocumentExist(formId: String): Boolean {
        val form = QWfFormEntity.wfFormEntity
        val document = QWfDocumentEntity.wfDocumentEntity
        val query = from(form)
            .innerJoin(document).on(document.form.formId.eq(form.formId)).fetchJoin()
            .where(
                form.formId.eq(formId)
                    .and(
                        document.documentStatus.`in`(
                            WfDocumentConstants.Status.USE.code,
                            WfDocumentConstants.Status.TEMPORARY.code
                        )
                    )
            )
        val result = query.fetchResults()
        return result.total > 0
    }
}
