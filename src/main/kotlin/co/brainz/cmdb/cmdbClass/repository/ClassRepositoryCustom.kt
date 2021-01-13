/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.cmdbClass.repository

import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface ClassRepositoryCustom : AliceRepositoryCustom {
    fun findClassList(search: String, offset: Long?): List<CmdbClassListDto>
}
