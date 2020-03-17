package co.brainz.workflow.engine.element.repository

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfElementRepository: JpaRepository<WfElementEntity, String> {

    @Query("SELECT elem.element, elem.attributeId, elem.attributeValue " +
            "FROM WfElementDataEntity elem " +
            "WHERE elem.attributeId = :startId " +
            "AND elem.attributeValue = :elementId")
    fun findAllArrowConnectorElement(elementId: String,
                                     startId: String = WfElementConstants.AttributeId.SOURCE_ID.value): MutableList<WfElementEntity>

    @Query("SELECT elem.element, elem.attributeId, elem.attributeValue " +
            "FROM WfElementDataEntity elem " +
            "WHERE elem.attributeId = :endId " +
            "AND elem.attributeValue = :elementId")
    fun findTargetElement(elementId: String,
                          endId: String = WfElementConstants.AttributeId.TARGET_ID.value): WfElementEntity

    fun findByProcessIdAndElementType(processId : String, elementType : String) : WfElementEntity

}
