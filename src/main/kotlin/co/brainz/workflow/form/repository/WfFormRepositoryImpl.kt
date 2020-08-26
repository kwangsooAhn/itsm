package co.brainz.workflow.form.repository

import co.brainz.itsm.constants.ItsmConstants
import co.brainz.workflow.form.entity.QWfFormEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.provider.constants.RestTemplateConstants
import com.querydsl.core.QueryResults
import com.querydsl.core.types.dsl.CaseBuilder
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfFormRepositoryImpl : QuerydslRepositorySupport(WfFormEntity::class.java),
    WfFormRepositoryCustom {

    override fun findFormEntityList(search: String, status: List<String>, offset: Long): QueryResults<WfFormEntity> {
        val form = QWfFormEntity.wfFormEntity
        val query = from(form)
            .innerJoin(form.createUser).fetchJoin()
            .leftJoin(form.updateUser).fetchJoin()
        if (search.isNotEmpty()) {
            query.where(form.formName.containsIgnoreCase(search).or(form.formDesc.containsIgnoreCase(search)))
        }
        if (status.isNotEmpty()) {
            query.where(form.formStatus.`in`(status)).orderBy(form.formName.asc())
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
        query.limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)

        return query.fetchResults()
    }
}
