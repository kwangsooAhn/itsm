/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.WfComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfComponentDataRepository : JpaRepository<WfComponentDataEntity, String>, WfComponentDataRepositoryCustom {
    @Query(
        "SELECT cd FROM WfComponentDataEntity cd " +
                "WHERE cd.componentId = :componentId AND cd.attributeId = :attributeId"
    )
    fun findByComponentIdAndAttributeId(componentId: String, attributeId: String): List<WfComponentDataEntity>

    @Query(
        "SELECT c FROM WfComponentDataEntity c WHERE c.componentId = :componentId"
    )
    fun findByComponentId(componentId: String): List<WfComponentDataEntity>
}
