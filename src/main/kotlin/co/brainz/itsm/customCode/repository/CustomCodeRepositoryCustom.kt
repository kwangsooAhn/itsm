/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.customCode.dto.CustomCodeListDto

interface CustomCodeRepositoryCustom : AliceRepositoryCustom {

    fun findByCustomCodeList(
        offset: Long,
        viewType: String?
    ): List<CustomCodeListDto>
}
