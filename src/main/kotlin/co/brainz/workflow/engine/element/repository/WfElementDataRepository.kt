package co.brainz.workflow.engine.element.repository

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementDataPk
import co.brainz.workflow.engine.element.entity.WfElementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfElementDataRepository : JpaRepository<WfElementDataEntity, WfElementDataPk> {

    fun findByElementAndAttributeId(
        element: WfElementEntity,
        endId: String = WfElementConstants.AttributeId.TARGET_ID.value
    ): WfElementDataEntity

    @Query(
        "SELECT e.elementId as elementId, ed.attributeValue as attributeValue " +
                "FROM WfElementEntity e, WfElementDataEntity ed " +
                "WHERE e.processId = :processId " +
                "AND e.elementId = ed.element.elementId " +
                "AND e.elementType = :elementType " +
                "AND ed.attributeId = :attributeId " +
                "ORDER BY e.elementId DESC"
    )
    fun findElementDataByProcessId(
        processId: String,
        elementType: String = WfElementConstants.ElementType.USER_TASK.value,
        attributeId: String = WfElementConstants.AttributeId.NAME.value
    ): List<Map<String, Any>>
}
