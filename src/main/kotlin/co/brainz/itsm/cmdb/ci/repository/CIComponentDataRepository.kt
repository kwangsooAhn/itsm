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
import org.springframework.transaction.annotation.Transactional

@Repository
interface CIComponentDataRepository : JpaRepository<CIComponentDataEntity, String> {
    @Query(
        "SELECT a FROM CIComponentDataEntity a WHERE a.ciId = :ciId AND a.componentId = :componentId"
    )
    fun findByCiIdAndComponentId(ciId: String, componentId: String): CIComponentDataEntity?

    @Transactional
    fun deleteByCiIdAndComponentId(ciId: String, componentId: String): Int

    fun findByComponentIdAndCiIdAndInstanceId(
        componentId: String,
        ciId: String,
        instanceId: String
    ): CIComponentDataEntity?

    fun findByInstanceId(instanceId: String): List<CIComponentDataEntity>?

    fun findByInstanceIdIn(instanceIds: MutableList<String>): List<CIComponentDataEntity>?
}
