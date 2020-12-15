package co.brainz.itsm.numberingPattern.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto

interface NumberingPatternRepositoryCustom : AliceRepositoryCustom {

    fun findPatternSearch(search: String): MutableList<NumberingPatternListDto>
}
