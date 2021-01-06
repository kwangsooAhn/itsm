/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.repository

import co.brainz.itsm.numberingRule.dto.NumberingRuleListDto
import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.itsm.numberingRule.entity.QNumberingRuleEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingRuleRepositoryImpl : QuerydslRepositorySupport(NumberingRuleEntity::class.java),
    NumberingRuleRepositoryCustom {

    override fun findRuleSearch(search: String): MutableList<NumberingRuleListDto> {
        val rule = QNumberingRuleEntity.numberingRuleEntity
        val query = from(rule)
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
                super.likeIgnoreCase(rule.numberingName, search)
            )
            .orderBy(rule.numberingName.desc())
            .fetchResults()

        val numberingRuleList = mutableListOf<NumberingRuleListDto>()
        for (data in query.results) {
            val numberingRuleListDto = NumberingRuleListDto(
                numberingId = data.numberingId,
                numberingName = data.numberingName,
                numberingDesc = data.numberingDesc,
                latestDate = data.latestDate,
                latestValue = data.latestValue
            )
            numberingRuleList.add(numberingRuleListDto)
        }
        return numberingRuleList
    }
}
