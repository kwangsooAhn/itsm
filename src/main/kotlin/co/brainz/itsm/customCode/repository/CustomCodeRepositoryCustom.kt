/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.customCode.dto.CustomCodeCoreDto
import co.brainz.itsm.customCode.dto.CustomCodeSearchCondition

interface CustomCodeRepositoryCustom : AliceRepositoryCustom {

    fun findByCustomCodeList(customCodeSearchCondition: CustomCodeSearchCondition): PagingReturnDto
    fun findByCustomCode(customCodeId: String): CustomCodeCoreDto
}
