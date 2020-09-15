package co.brainz.workflow.process.repository

import co.brainz.itsm.constants.ItsmConstants
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

    override fun findProcessEntityList(
        search: String,
        status: List<String>,
        offset: Long?
    ): QueryResults<WfProcessEntity> {
        val process = QWfProcessEntity.wfProcessEntity
        val query = from(process)
            .innerJoin(process.createUser).fetchJoin()
            .leftJoin(process.updateUser).fetchJoin()
        if (search.isNotEmpty()) {
            query.where(
                process.processName.containsIgnoreCase(search).or(process.processDesc.containsIgnoreCase(search))
            )
        }
        if (status.isNotEmpty()) {
            query.where(process.processStatus.`in`(status)).orderBy(process.processName.asc())
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
        if (offset != null) {
            query.limit(ItsmConstants.SEARCH_DATA_COUNT)
                .offset(offset)
        }
        return query.fetchResults()
    }
}
