package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.FormMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FormRepository: JpaRepository<FormMstEntity, String>, FormRepositoryCustom {

    fun findFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContaining(formName: String, formDesc: String): List<FormMstEntity>
    fun findFormEntityByFormId(formId: String): Optional<FormMstEntity>
    fun removeFormEntityByFormId(formId: String)
}
