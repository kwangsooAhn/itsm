package co.brainz.workflow.engine.form.repository

import co.brainz.workflow.engine.form.entity.WfFormEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface WfFormRepository: JpaRepository<WfFormEntity, String>, WfFormRepositoryCustom {

    @Query("SELECT m " +
            "FROM WfFormEntity m " +
            "WHERE (lower(m.formName) like lower(concat('%', :value, '%')) or lower(m.formDesc) like lower(concat('%', :value, '%')) or :value is null or :value = '') " +
            "ORDER BY " +
            "CASE " +
            "WHEN m.formStatus = 'form.status.edit' THEN 1 " +
            "WHEN m.formStatus = 'form.status.publish' THEN 2 " +
            "WHEN m.formStatus = 'form.status.destroy' THEN 3 " +
            "END, COALESCE(m.updateDt, m.createDt) DESC")
    fun findFormListOrFormSearchList(value: String?): List<WfFormEntity>
    fun findWfFormEntityByFormId(formId: String): Optional<WfFormEntity>
    fun findWfFormEntityByFormStatus(formStatus: String): List<WfFormEntity>
    fun removeWfFormEntityByFormId(formId: String)
}
