/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.dto.CIGroupListDataDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIGroupListDataRepositoryCustom : AliceRepositoryCustom {
    fun getCIGroupListDataList(
        ciIds: Set<String>,
        attributeId: String,
        cAttributeIds: Set<String>
    ): List<CIGroupListDataDto>
}
