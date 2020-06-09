package co.brainz.workflow.element.repository

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementDataPk
import co.brainz.workflow.element.entity.WfElementEntity
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
    "SELECT e.elementId as elementId, case e.elementName when '' then e.elementType else e.elementName end as elementName " +
          "FROM WfElementEntity e " +
          "WHERE e.processId = :processId " +
          "AND e.elementType = :elementType " +
          "ORDER BY e.elementId DESC"
    )
    fun findElementDataByProcessId(
        processId: String,
        elementType: String = WfElementConstants.ElementType.USER_TASK.value
    ): List<Map<String, Any>>
}
