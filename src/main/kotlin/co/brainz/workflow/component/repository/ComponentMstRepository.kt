package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.ComponentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ComponentMstRepository: JpaRepository<ComponentEntity, String> {

    @Query("select f from ComponentEntity f where f.components.formId = :formId")
    fun findByFormId(formId: String): List<ComponentEntity>

    fun deleteComponentMstEntityByComponentIdIn(componentIds: MutableList<String>)




}

