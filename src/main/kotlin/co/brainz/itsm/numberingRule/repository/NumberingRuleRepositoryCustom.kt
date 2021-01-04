package co.brainz.itsm.numberingRule.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.numberingRule.dto.NumberingRuleListDto

interface NumberingRuleRepositoryCustom : AliceRepositoryCustom {

    fun findRuleSearch(search: String): MutableList<NumberingRuleListDto>
}
