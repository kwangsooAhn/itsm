package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.ComponentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ComponentRepository : JpaRepository<ComponentEntity, String> {

    @Query("select f from ComponentEntity f where f.form.formId = :formId")
    fun findByFormId(formId: String): List<ComponentEntity>

    fun deleteComponentEntityByComponentIdIn(componentIds: MutableList<String>)

}

