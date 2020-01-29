package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.FormEntity
import java.util.Optional

interface FormRepositoryCustom {

    fun findFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContaining(formName: String, formDesc: String): List<FormEntity>
    fun findFormEntityByFormId(formId: String): Optional<FormEntity>
    fun removeFormEntityByFormId(formId: String)

}
