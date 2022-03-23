/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIDataRepositoryCustom : AliceRepositoryCustom {
    fun findCIDataList(ciIds: Set<String>): List<CIDataEntity>
}
