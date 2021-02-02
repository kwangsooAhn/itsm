/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.entity.CIDataPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CIDataRepository : JpaRepository<CIDataEntity, CIDataPk> {
    // CI Id 에 해당되는 CI_Data 삭제
    @Transactional
    @Modifying
    @Query(
        "DELETE FROM CIDataEntity ciData WHERE ciData.ci.ciId = :ciId"
    )
    fun deleteByCiId(ciId: String)
}
