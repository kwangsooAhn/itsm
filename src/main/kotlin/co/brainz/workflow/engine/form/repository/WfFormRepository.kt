package co.brainz.workflow.engine.form.repository

import co.brainz.workflow.engine.form.entity.WfFormEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface WfFormRepository: JpaRepository<WfFormEntity, String>, WfFormRepositoryCustom {

    fun findWfFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContainingOrderByCreateDtDesc(formName: String, formDesc: String): List<WfFormEntity>
    fun findWfFormEntityByFormId(formId: String): Optional<WfFormEntity>
    fun removeWfFormEntityByFormId(formId: String)
    @Query("SELECT m " +
            "FROM WfFormEntity m " +
            "ORDER BY CASE " +
            "WHEN m.formStatus = 'form.status.edit' THEN 1 " +
            "WHEN m.formStatus = 'form.status.publish' THEN 2 " +
            "WHEN m.formStatus = 'form.status.destroy' THEN 3 " +
            "END, m.createDt DESC")
    fun findByFormList() : List<WfFormEntity>
}
