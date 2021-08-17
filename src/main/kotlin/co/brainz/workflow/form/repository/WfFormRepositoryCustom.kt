package co.brainz.workflow.form.repository

import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.workflow.form.entity.WfFormEntity
import com.querydsl.core.QueryResults

interface WfFormRepositoryCustom {

    fun findFormEntityList(formSearchCondition: FormSearchCondition): QueryResults<WfFormEntity>

    fun findFormDocumentExist(formId: String): Boolean
}
