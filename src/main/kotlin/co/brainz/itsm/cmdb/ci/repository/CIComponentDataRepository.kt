/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.repository

import co.brainz.itsm.cmdb.ci.entity.CIComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CIComponentDataRepository : JpaRepository<CIComponentDataEntity, String> {
    @Query(
        "SELECT a FROM WfCIComponentDataEntity a WHERE a.ciId = :ciId AND a.componentId = :componentId"
    )
    fun findByCiIdAnAndComponentId(ciId: String, componentId: String): CIComponentDataEntity?

    fun deleteByCiIdAndAndComponentId(ciId: String, componentId: String)

    fun findByComponentIdAndCiIdAndInstanceId(
        ciId: String,
        componentId: String,
        instanceId: String
    ): CIComponentDataEntity?

    fun findByInstanceId(instanceId: String): List<CIComponentDataEntity>?
}
