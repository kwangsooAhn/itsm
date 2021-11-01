package co.brainz.workflow.process.repository

import co.brainz.itsm.process.dto.ProcessSearchCondition
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.process.entity.QWfProcessEntity
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.provider.constants.RestTemplateConstants
import com.querydsl.core.QueryResults
import com.querydsl.core.types.dsl.CaseBuilder
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfProcessRepositoryImpl : QuerydslRepositorySupport(WfProcessEntity::class.java),
    WfProcessRepositoryCustom {

    override fun findProcessEntityList(processSearchCondition: ProcessSearchCondition): QueryResults<WfProcessEntity> {
        val process = QWfProcessEntity.wfProcessEntity
        val query = from(process)
            .innerJoin(process.createUser).fetchJoin()
            .leftJoin(process.updateUser).fetchJoin()
        if (processSearchCondition.searchValue?.isNotEmpty() == true) {
            query.where(
                process.processName.containsIgnoreCase(processSearchCondition.searchValue.trim())
                    .or(process.processDesc.containsIgnoreCase(processSearchCondition.searchValue.trim()))
            )
        }
        if (processSearchCondition.statusArray?.isNotEmpty() == true) {
            query.where(process.processStatus.`in`(processSearchCondition.statusArray)).orderBy(process.processName.asc())
        } else {
            val statusNumber = CaseBuilder()
                .`when`(process.processStatus.eq(RestTemplateConstants.ProcessStatus.EDIT.value)).then(1)
                .`when`(process.processStatus.eq(RestTemplateConstants.ProcessStatus.PUBLISH.value)).then(2)
                .`when`(process.processStatus.eq(RestTemplateConstants.ProcessStatus.USE.value)).then(3)
                .`when`(process.processStatus.eq(RestTemplateConstants.ProcessStatus.DESTROY.value)).then(4)
                .otherwise(5)
            query.orderBy(statusNumber.asc())
                .orderBy(process.updateDt.coalesce(process.createDt).desc())
        }
        if (processSearchCondition.isPaging) {
            query.limit(processSearchCondition.contentNumPerPage)
            query.offset((processSearchCondition.pageNum - 1) * processSearchCondition.contentNumPerPage)
        }

        return query.fetchResults()
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
        val result = query.fetchResults()
        return result.total > 0
    }
}
