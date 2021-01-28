package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CIComponentDataRepository : JpaRepository<CIComponentDataEntity, String> {
    @Query(
    "SELECT a FROM CIComponentDataEntity a WHERE a.ciId = :ciId AND a.componentId = :componentId"
    )
    fun findByCiIdAnAndComponentId(ciId: String, componentId: String): CIComponentDataEntity?

    fun deleteByCiIdAndAndComponentId(ciId: String, componentId: String)
}