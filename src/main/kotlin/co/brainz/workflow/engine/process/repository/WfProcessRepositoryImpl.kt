package co.brainz.workflow.engine.process.repository

import co.brainz.workflow.engine.process.entity.QWfProcessEntity
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import co.brainz.workflow.provider.constants.RestTemplateConstants
import com.querydsl.core.types.dsl.CaseBuilder
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfProcessRepositoryImpl : QuerydslRepositorySupport(WfProcessEntity::class.java), WfProcessRepositoryCustom {

    override fun findProcessEntityList(search: String, status: List<String>): List<WfProcessEntity> {
        val process = QWfProcessEntity.wfProcessEntity
        val query = from(process)
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
        return query.fetch()
    }
}
