package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.FormEntity
//import co.brainz.workflow.form.entity.QFormEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FormRepositoryImpl: QuerydslRepositorySupport(FormEntity::class.java), FormRepositoryCustom {

    /*override fun findFormEntityList(formName: String, formDesc: String): List<FormEntity> {
        val table = QFormEntity.formEntity
        return from(table)
                .where(table.formName.containsIgnoreCase(formName).or(table.formDesc.containsIgnoreCase(formDesc)))
                .fetch()
    }*/

}
