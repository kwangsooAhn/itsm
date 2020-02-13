package co.brainz.workflow.form.service

import co.brainz.workflow.form.dto.FormComponentDto
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
     * Search Form.
     *
     * @param formId
     * @return FormDto
     */
    fun form(formId: String): FormDto

    /**
     * Insert Form.
     *
     * @param formDto
     * @return FormDto
     */
    fun insertForm(formDto: FormDto): FormDto

    /**
     * Update Form.
     *
     * @param formDto
     */
    fun updateForm(formDto: FormDto)

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
    fun formComponents(formId: String): FormComponentDto

}
