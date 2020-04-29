package co.brainz.workflow.engine.element.repository

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfElementRepository : JpaRepository<WfElementEntity, String> {

    @Query(
        "SELECT elem.element, elem.attributeId, elem.attributeValue " +
                "FROM WfElementDataEntity elem " +
                "WHERE elem.attributeId = :startId " +
                "AND elem.attributeValue = :elementId"
    )
    fun findAllArrowConnectorElement(
        elementId: String,
        startId: String = WfElementConstants.AttributeId.SOURCE_ID.value
    ): MutableList<WfElementEntity>

    @Query(
        "SELECT elem " +
                "FROM WfElementEntity elem " +
                "WHERE elem.processId = :processId " +
                "AND elem.elementType = :elementType"
    )
    fun findUserTaskByProcessId(
        processId: String,
        elementType: String = WfElementConstants.ElementType.USER_TASK.value
    ): MutableList<WfElementEntity>


    fun findByProcessIdAndElementType(processId: String, elementType: String): WfElementEntity

    fun findWfElementEntityByElementId(elementId: String): WfElementEntity
}
