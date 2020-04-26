package co.brainz.workflow.engine.component.repository

import co.brainz.workflow.engine.component.entity.WfComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfComponentDataRepository: JpaRepository<WfComponentDataEntity, String> {
    @Query("SELECT cd FROM WfComponentEntity c, WfComponentDataEntity cd " +
                 "WHERE c.componentId = cd.componentId AND c.componentType = :componentType")
    fun findByComponentDataList(componentType: String?): List<WfComponentDataEntity>

    @Query("SELECT cd " +
            "FROM  WfComponentDataEntity cd " +
            "WHERE cd.componentId = :componentId " +
            "AND cd.attributeId = :attributeId")
    fun findComponentDataByComponentId(componentId: String, attributeId: String): List<WfComponentDataEntity>
}