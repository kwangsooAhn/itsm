package co.brainz.workflow.engine.component.repository

import co.brainz.workflow.engine.component.entity.WfComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfComponentDataRepository : JpaRepository<WfComponentDataEntity, String> {
    @Query(
        "SELECT cd FROM WfComponentDataEntity cd " +
                "WHERE cd.componentId = :componentId AND cd.attributeId = :attributeId"
    )
    fun findComponentDataByComponentId(componentId: String, attributeId: String): List<WfComponentDataEntity>

    /**
     * 커스텀 코드 타입의 display 데이터 정보를 리턴.
     */
    @Query(
        "SELECT cd FROM WfComponentEntity c join WfComponentDataEntity cd " +
                "ON c.componentType = :componentType AND c.componentId = cd.componentId AND cd.attributeId = :componentAttribute"
    )
    fun findAllCustomCodeDisplayAttr(componentType: Any, componentAttribute: Any): List<WfComponentDataEntity>
}
