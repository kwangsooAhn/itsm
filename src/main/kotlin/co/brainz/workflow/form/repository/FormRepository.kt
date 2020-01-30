package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.FormEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FormRepository: JpaRepository<FormEntity, String>, FormRepositoryCustom {

    fun findFormEntityByFormId(formId: String): Optional<FormEntity>
    fun removeFormEntityByFormId(formId: String)
}
