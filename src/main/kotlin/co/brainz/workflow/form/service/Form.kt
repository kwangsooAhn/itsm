package co.brainz.workflow.form.service

import co.brainz.workflow.form.dto.FormComponentSaveDto
import co.brainz.workflow.form.dto.FormComponentViewDto
import co.brainz.workflow.form.dto.FormDto

interface Form {

    /**
     * Search Forms.
     *
     * @param search
     * @return List<FormDto>
     */
    fun forms(search: String): List<FormDto>

    /**
     * Create Form.
     *
     * @param formDto
     * @return FormDto
     */
    fun createForm(formDto: FormDto): FormDto

    /**
     * Delete Form.
     *
     * @param formId
     */
    fun deleteForm(formId: String)

    /**
     * Search Form + Components.
     *
     * @param formId
     */
    fun form(formId: String): FormComponentViewDto

    /**
     * Insert Form.
     *
     * @param formComponentSaveDto
     */
    fun saveForm(formComponentSaveDto: FormComponentSaveDto)

}
