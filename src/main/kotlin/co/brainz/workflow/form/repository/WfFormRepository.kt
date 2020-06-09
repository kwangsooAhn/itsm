package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.WfFormEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository

interface WfFormRepository : JpaRepository<WfFormEntity, String>,
    WfFormRepositoryCustom {

    fun findWfFormEntityByFormId(formId: String): Optional<WfFormEntity>
    fun removeWfFormEntityByFormId(formId: String)
}
