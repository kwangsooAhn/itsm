package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.WfComponentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfComponentRepository : JpaRepository<WfComponentEntity, String> {

    @Query("select f from WfComponentEntity f where f.form.formId = :formId")
    fun findByFormId(formId: String): List<WfComponentEntity>

    @Query("select f from WfComponentEntity f where f.form.formId = :formId and f.componentType <> :componentType")
    fun findByFormIdAndComponentTypeNot(formId: String, componentType: String): List<WfComponentEntity>

    fun deleteComponentEntityByComponentIdIn(componentIds: MutableList<String>)

    fun findByComponentIdInAndMappingId(componentIds: List<String>, mappingId: String): WfComponentEntity

    @Query(
        "SELECT c FROM WfComponentEntity c WHERE c.form.formId = :formId AND c.isTopic = :isTopic " +
                "AND c.componentType IN :componentTypes"
    )
    fun findTopicComponentForDisplay(
        formId: String,
        isTopic: Boolean,
        componentTypes: List<String>
    ): List<WfComponentEntity>
}
