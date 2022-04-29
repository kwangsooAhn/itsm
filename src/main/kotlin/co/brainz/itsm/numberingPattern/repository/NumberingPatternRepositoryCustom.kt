/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternSearchCondition

interface NumberingPatternRepositoryCustom : AliceRepositoryCustom {
    fun findPatternSearch(numberingPatternSearchCondition: NumberingPatternSearchCondition): PagingReturnDto
}
