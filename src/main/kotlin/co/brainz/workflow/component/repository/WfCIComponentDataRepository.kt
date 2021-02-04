/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.WfCIComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfCIComponentDataRepository : JpaRepository<WfCIComponentDataEntity, String> {
    @Query(
        "SELECT a FROM WfCIComponentDataEntity a WHERE a.ciId = :ciId AND a.componentId = :componentId"
    )
    fun findByCiIdAnAndComponentId(ciId: String, componentId: String): WfCIComponentDataEntity?

    fun deleteByCiIdAndAndComponentId(ciId: String, componentId: String)

    fun findByComponentIdAndCiIdAndInstanceId(
        ciId: String,
        componentId: String,
        instanceId: String
    ): WfCIComponentDataEntity?

    fun findByInstanceId(instanceId: String): List<WfCIComponentDataEntity>?
}
