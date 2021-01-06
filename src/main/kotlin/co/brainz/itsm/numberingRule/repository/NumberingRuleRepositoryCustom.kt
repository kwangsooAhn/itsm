/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.numberingRule.dto.NumberingRuleListDto

interface NumberingRuleRepositoryCustom : AliceRepositoryCustom {

    fun findRuleSearch(search: String): MutableList<NumberingRuleListDto>
}
