/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.customCode.dto.CustomCodeCoreDto
import co.brainz.itsm.customCode.dto.CustomCodeListReturnDto
import co.brainz.itsm.customCode.dto.CustomCodeSearchDto

interface CustomCodeRepositoryCustom : AliceRepositoryCustom {

    fun findByCustomCodeList(customCodeSearchDto: CustomCodeSearchDto): CustomCodeListReturnDto

    fun findByCustomCode(customCodeId: String): CustomCodeCoreDto
}
