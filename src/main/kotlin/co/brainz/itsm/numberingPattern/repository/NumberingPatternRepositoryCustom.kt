/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternSearchCondition
import com.querydsl.core.QueryResults

interface NumberingPatternRepositoryCustom : AliceRepositoryCustom {
    fun findPatternSearch(numberingPatternSearchCondition: NumberingPatternSearchCondition): QueryResults<NumberingPatternListDto>
}
