package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.FormEntity
import co.brainz.workflow.form.entity.QFormEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import java.util.Optional

class FormRepositoryImpl: QuerydslRepositorySupport(FormEntity::class.java), FormRepositoryCustom {

    override fun findFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContaining(formName: String, formDesc: String): List<FormEntity> {
        val table = QFormEntity.formEntity
        return from(table).where(table.formName.containsIgnoreCase(formName).or(table.formDesc.containsIgnoreCase(formDesc))).fetch()
    }

    override fun findFormEntityByFormId(formId: String): Optional<FormEntity> {
        return findFormEntityByFormId(formId)
    }

    override fun removeFormEntityByFormId(formId: String) {
        return removeFormEntityByFormId(formId)
    }

}
