/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciRelation.repository

import co.brainz.cmdb.ciRelation.entity.CIRelationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CIRelationRepository : JpaRepository<CIRelationEntity, String>, CIRelationRepositoryCustom {
    // CI Id 에 해당되는 CI_Relation 삭제
    @Transactional
    @Modifying
    @Query(
        "DELETE FROM CIRelationEntity ciRelation WHERE ciRelation.masterCIId = :ciId " +
                "OR ciRelation.slaveCIId = :ciId"
    )
    fun deleteByCiId(ciId: String)
}
