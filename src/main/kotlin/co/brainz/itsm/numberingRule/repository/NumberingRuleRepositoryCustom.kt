/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.numberingRule.dto.NumberingRuleListDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleSearchCondition
import com.querydsl.core.QueryResults
import org.springframework.data.domain.Page

interface NumberingRuleRepositoryCustom : AliceRepositoryCustom {
    fun findRuleSearch(numberingRuleSearchCondition: NumberingRuleSearchCondition): Page<NumberingRuleListDto>
}
