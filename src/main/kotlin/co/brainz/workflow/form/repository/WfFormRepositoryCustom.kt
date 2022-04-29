package co.brainz.workflow.form.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.form.dto.FormSearchCondition

interface WfFormRepositoryCustom {

    fun findFormEntityList(formSearchCondition: FormSearchCondition): PagingReturnDto

    fun findFormDocumentExist(formId: String): Boolean
}
