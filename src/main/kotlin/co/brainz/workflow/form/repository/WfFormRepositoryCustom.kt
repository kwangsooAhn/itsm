package co.brainz.workflow.form.repository

import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.workflow.form.entity.WfFormEntity
import com.querydsl.core.QueryResults
import org.springframework.data.domain.Page

interface WfFormRepositoryCustom {

    fun findFormEntityList(formSearchCondition: FormSearchCondition): Page<WfFormEntity>

    fun findFormDocumentExist(formId: String): Boolean
}
