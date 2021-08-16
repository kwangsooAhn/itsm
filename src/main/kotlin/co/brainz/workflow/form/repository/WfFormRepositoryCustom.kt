package co.brainz.workflow.form.repository

import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.provider.dto.RestTemplateFormListReturnDto
import com.querydsl.core.QueryResults

interface WfFormRepositoryCustom {

    fun findFormEntityList(formSearchCondition: FormSearchCondition): RestTemplateFormListReturnDto

    fun findFormDocumentExist(formId: String): Boolean
}
