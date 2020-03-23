package co.brainz.workflow.engine.form.service

import co.brainz.workflow.engine.form.dto.WfFormComponentDataDto
import co.brainz.workflow.engine.form.dto.WfFormComponentSaveDto
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.dto.WfFormDto

interface WfForm {

    /**
     * Search Forms.
     *
     * @param parameters
     * @return List<FormDto>
     */
    fun forms(parameters: LinkedHashMap<String, Any>): List<WfFormDto>

    /**
     * Create Form.
     *
     * @param wfFormDto
     * @return FormDto
     */
    fun createForm(wfFormDto: WfFormDto): WfFormDto

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
    fun form(formId: String): WfFormComponentViewDto

    /**
     * Insert Form.
     *
     * @param wfFormComponentSaveDto
     */
    fun saveForm(wfFormComponentSaveDto: WfFormComponentSaveDto)

    /**
     * Save as Form.
     *
     * @param wfFormComponentSaveDto
     */
    fun saveAsForm(wfFormComponentSaveDto: WfFormComponentSaveDto): WfFormDto

    /**
     * Get Component Data.
     *
     * @param componentType
     * @return List<WfFormComponentDataDto>
     */
    fun getFormComponentData(componentType: String): List<WfFormComponentDataDto>
}
