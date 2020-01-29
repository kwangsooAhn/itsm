package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.FormEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FormRepository: JpaRepository<FormEntity, String> {

    fun findFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContaining(formName: String, formDesc: String): List<FormEntity>
    fun findFormEntityByFormId(formId: String): Optional<FormEntity>
    fun removeFormEntityByFormId(formId: String)

}
