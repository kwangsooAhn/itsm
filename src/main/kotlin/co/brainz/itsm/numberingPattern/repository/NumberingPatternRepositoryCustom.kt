/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListReturnDto

interface NumberingPatternRepositoryCustom : AliceRepositoryCustom {

    fun findPatternSearch(search: String): NumberingPatternListReturnDto
}
