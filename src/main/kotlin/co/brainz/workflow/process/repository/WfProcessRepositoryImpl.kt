/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.process.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.process.dto.ProcessSearchCondition
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.process.entity.QWfProcessEntity
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfProcessRepositoryImpl : QuerydslRepositorySupport(WfProcessEntity::class.java),
    WfProcessRepositoryCustom {

    override fun findProcessEntityList(processSearchCondition: ProcessSearchCondition): PagingReturnDto {
        val process = QWfProcessEntity.wfProcessEntity
        val user = QAliceUserEntity.aliceUserEntity
        val query = from(process)
            .select(
                Projections.constructor(
                    RestTemplateProcessViewDto::class.java,
                    process.processId,
                    process.processName,
                    process.processDesc,
                    process.processStatus,
                    process.createDt,
                    process.createUser.userKey,
                    process.createUser.userName,
                    process.updateDt,
                    process.updateUser.userKey,
                    process.updateUser.userName,
                    Expressions.asBoolean(true)
                )
            )
            .innerJoin(process.createUser,user)
            .leftJoin(process.updateUser, user)
            .where(builder(processSearchCondition,process))
        if (processSearchCondition.isPaging) {
            query.limit(processSearchCondition.contentNumPerPage)
            query.offset((processSearchCondition.pageNum - 1) * processSearchCondition.contentNumPerPage)
        }

        val countQuery = from(process)
            .select(process.count())
            .where(builder(processSearchCondition,process))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    override fun findProcessDocumentExist(processId: String): Boolean {
        val process = QWfProcessEntity.wfProcessEntity
        val document = QWfDocumentEntity.wfDocumentEntity
        val query = from(process)
            .innerJoin(document).on(document.process.processId.eq(process.processId)).fetchJoin()
            .where(
                process.processId.eq(processId)
                    .and(
                        document.documentStatus.`in`(
                            WfDocumentConstants.Status.USE.code,
                            WfDocumentConstants.Status.TEMPORARY.code
                        )
                    )
            )
        val result = query.fetch()
        return result.size > 0
    }

    private fun builder(processSearchCondition: ProcessSearchCondition, process: QWfProcessEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        if (processSearchCondition.searchValue?.isNotEmpty() == true) {
            builder.and(
                process.processName.containsIgnoreCase(processSearchCondition.searchValue.trim())
                    .or(process.processDesc.containsIgnoreCase(processSearchCondition.searchValue.trim()))
            )
        }
            if (processSearchCondition.statusArray?.isNotEmpty() == true) {
                builder.and(process.processStatus.`in`(processSearchCondition.statusArray))
            } else {
                CaseBuilder()
                    .`when`(process.processStatus.eq(WorkflowConstants.ProcessStatus.EDIT.value)).then(1)
                    .`when`(process.processStatus.eq(WorkflowConstants.ProcessStatus.PUBLISH.value)).then(2)
                    .`when`(process.processStatus.eq(WorkflowConstants.ProcessStatus.USE.value)).then(3)
                    .`when`(process.processStatus.eq(WorkflowConstants.ProcessStatus.DESTROY.value)).then(4)
                    .otherwise(5)
            }
        return builder
    }
}
