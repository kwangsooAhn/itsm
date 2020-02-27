package co.brainz.workflow.element.repository

import co.brainz.workflow.element.constants.ElementConstants
import co.brainz.workflow.element.entity.ElementMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ElementMstRepository: JpaRepository<ElementMstEntity, String> {

    @Query("SELECT elem " +
            "FROM wf_elem_data elem " +
            "WHERE attr_id = :startId " +
            "AND sub.attr_value = :elementId")
    fun findAllArrowConnectorElement(elementId: String,
                        startId: String = ElementConstants.AttributeId.SOURCE_ID.value): MutableList<ElementMstEntity>

    @Query("SELECT elem " +
            "FROM wf_elem_data elem " +
            "WHERE attr_id = :endId " +
            "AND sub.attr_value = :elementId")
    fun findTargetElement(elementId: String,
                                     endId: String = ElementConstants.AttributeId.TARGET_ID.value): ElementMstEntity

}