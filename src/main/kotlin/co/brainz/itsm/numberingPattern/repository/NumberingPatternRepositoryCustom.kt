package co.brainz.itsm.numberingPattern.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListReturnDto

interface NumberingPatternRepositoryCustom : AliceRepositoryCustom {

    fun findPatternSearch(search: String): NumberingPatternListReturnDto
}
