package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.WfComponentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfComponentRepository : JpaRepository<WfComponentEntity, String> {

    @Query("select f from WfComponentEntity f where f.form.formId = :formId")
    fun findByFormId(formId: String): List<WfComponentEntity>

    fun deleteComponentEntityByComponentIdIn(componentIds: MutableList<String>)

}

