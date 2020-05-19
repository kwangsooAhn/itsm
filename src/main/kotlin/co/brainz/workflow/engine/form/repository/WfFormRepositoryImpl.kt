package co.brainz.workflow.engine.form.repository

import co.brainz.workflow.engine.form.entity.QWfFormEntity
import co.brainz.workflow.engine.form.entity.WfFormEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfFormRepositoryImpl : QuerydslRepositorySupport(WfFormEntity::class.java), WfFormRepositoryCustom {

    override fun findFormEntityList(search: String, status: List<String>): List<WfFormEntity> {
        val form = QWfFormEntity.wfFormEntity
        val query = from(form)
        if (search.isNotEmpty()) {
            query.where(form.formName.containsIgnoreCase(search).or(form.formDesc.containsIgnoreCase(search)))
        }
        if (status.isNotEmpty()) {
            query.where(form.formStatus.`in`(status))
                .orderBy(form.formName.asc())
        } else {
            query.orderBy(form.updateDt.desc())
                .orderBy(form.createDt.desc())
        }
        return query.fetch()
    }
}
