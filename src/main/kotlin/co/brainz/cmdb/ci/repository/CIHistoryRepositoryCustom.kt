/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIHistoryEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIHistoryRepositoryCustom : AliceRepositoryCustom {
    fun findByLatelyHistory(ciId: String): CIHistoryEntity?
}
