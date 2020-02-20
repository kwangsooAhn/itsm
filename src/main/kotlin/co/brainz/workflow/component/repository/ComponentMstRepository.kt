package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.ComponentMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ComponentMstRepository: JpaRepository<ComponentMstEntity, String> {

    @Query("select f from ComponentMstEntity f where f.components.formId = :formId")
    fun findByFormId(formId: String): List<ComponentMstEntity>

    fun deleteComponentMstEntityByCompIdIn(componentIds: MutableList<String>)




}

