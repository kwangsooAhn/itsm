/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.repository

import co.brainz.itsm.numberingRule.dto.NumberingRuleListDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleSearchCondition
import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.itsm.numberingRule.entity.QNumberingRuleEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingRuleRepositoryImpl : QuerydslRepositorySupport(NumberingRuleEntity::class.java),
    NumberingRuleRepositoryCustom {

    override fun findRuleSearch(numberingRuleSearchCondition: NumberingRuleSearchCondition): QueryResults<NumberingRuleListDto> {
        val rule = QNumberingRuleEntity.numberingRuleEntity
        return from(rule)
            .select(
                Projections.constructor(
                    NumberingRuleListDto::class.java,
                    rule.numberingId,
                    rule.numberingName,
                    rule.numberingDesc,
                    rule.latestDate,
                    rule.latestValue
                )
            )
            .where(
                super.like(rule.numberingName, numberingRuleSearchCondition.searchValue)
            )
            .orderBy(rule.numberingName.desc())
            .limit(numberingRuleSearchCondition.contentNumPerPage)
            .offset((numberingRuleSearchCondition.pageNum - 1) * numberingRuleSearchCondition.contentNumPerPage)
            .fetchResults()
    }
}
