/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIGroupListDataEntity
import co.brainz.cmdb.ci.entity.CIGroupListDataPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CIGroupListDataRepository : JpaRepository<CIGroupListDataEntity, CIGroupListDataPk> {
    // ci Id, attribute Id 에 해당되는 cmdb_ci_group_list_data 삭제
    fun deleteByCi_CiIdAndCiAttribute_AttributeId(ciId: String, attributeId: String)
}
