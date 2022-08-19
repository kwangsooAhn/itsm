/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.cmdb.ci.dto.CICapacityDto

interface CICapacityRepositoryCustom : AliceRepositoryCustom {
    fun findCapacityChartData(ciId: String): List<CICapacityDto>
}
