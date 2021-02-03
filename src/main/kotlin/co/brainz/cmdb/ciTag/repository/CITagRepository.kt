/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciTag.repository

import co.brainz.cmdb.ciTag.entity.CITagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CITagRepository : JpaRepository<CITagEntity, String>, CITagRepositoryCustom {
    // CI Id 에 해당되는 CI Tag 삭제
    @Transactional
    @Modifying
    @Query(
        "DELETE FROM CITagEntity ciTag WHERE ciTag.ci.ciId = :ciId"
    )
    fun deleteByCiId(ciId: String)
}
