package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.WfComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfComponentDataRepository : JpaRepository<WfComponentDataEntity, String> {
    @Query(
        "SELECT cd FROM WfComponentDataEntity cd " +
                "WHERE cd.componentId = :componentId AND cd.attributeId = :attributeId"
    )
    fun findByComponentIdAndAttributeId(componentId: String, attributeId: String): List<WfComponentDataEntity>

    /**
     * 커스텀 코드 타입의 display 데이터 정보를 리턴.
     */
    @Query(
        "SELECT distinct cd FROM WfComponentEntity c join WfComponentDataEntity cd " +
                "ON c.componentType = :componentType AND c.componentId = cd.componentId AND cd.attributeId = :attributeId"
    )
    fun findAllByComponentTypeAndAttributeId(componentType: Any, attributeId: Any): List<WfComponentDataEntity>

    @Query(
        "SELECT c FROM WfComponentDataEntity c WHERE c.componentId = :componentId"
    )
    fun findByComponentId(componentId: String): List<WfComponentDataEntity>
}
