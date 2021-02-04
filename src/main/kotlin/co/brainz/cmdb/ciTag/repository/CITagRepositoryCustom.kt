/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciTag.repository

import co.brainz.cmdb.provider.dto.CITagDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CITagRepositoryCustom : AliceRepositoryCustom {
    fun findByCIId(ciId: String): MutableList<CITagDto>
}
