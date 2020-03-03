package co.brainz.workflow.element.repository

import co.brainz.workflow.element.entity.ElementMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ElementMstRepository: JpaRepository<ElementMstEntity, String> {
// TODO 2020-03-03 kbh - 아래 메소드로 인하여 ElementMstRepository.save(eneity) 가 동작을 안함. 확인 바랍니다.
//    @Query("SELECT elem " +
//            "FROM wf_elem_data elem " +
//            "WHERE attr_id = :startId " +
//            "AND sub.attr_value = :elementId")
//    fun findAllArrowConnectorElement(elementId: String,
//                        startId: String = ElementConstants.AttributeId.SOURCE_ID.value): MutableList<ElementMstEntity>
//
//    @Query("SELECT elem " +
//            "FROM wf_elem_data elem " +
//            "WHERE attr_id = :endId " +
//            "AND sub.attr_value = :elementId")
//    fun findTargetElement(elementId: String,
//                                     endId: String = ElementConstants.AttributeId.TARGET_ID.value): ElementMstEntity

}
