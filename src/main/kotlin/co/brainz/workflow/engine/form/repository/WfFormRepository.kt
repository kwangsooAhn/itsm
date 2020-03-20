package co.brainz.workflow.engine.form.repository

import co.brainz.workflow.engine.form.entity.WfFormEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface WfFormRepository: JpaRepository<WfFormEntity, String>, WfFormRepositoryCustom {

    fun findWfFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContainingOrderByCreateDtDesc(formName: String, formDesc: String): List<WfFormEntity>
    fun findWfFormEntityByFormId(formId: String): Optional<WfFormEntity>
    fun findWfFormEntityByFormStatus(formStatus: String): List<WfFormEntity>
    fun removeWfFormEntityByFormId(formId: String)
}
