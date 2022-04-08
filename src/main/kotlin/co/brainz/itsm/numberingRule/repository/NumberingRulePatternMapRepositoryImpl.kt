/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.repository

import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.itsm.numberingRule.entity.NumberingRulePatternMapEntity
import co.brainz.itsm.numberingRule.entity.QNumberingRulePatternMapEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingRulePatternMapRepositoryImpl : QuerydslRepositorySupport(NumberingRuleEntity::class.java),
    NumberingRulePatternMapRepositoryCustom {

    override fun findAllByNumberingPatternIn(patternList: MutableList<String>, numberingId: String): QueryResults<NumberingRulePatternMapEntity> {
        val numberingRulePatternMap = QNumberingRulePatternMapEntity.numberingRulePatternMapEntity
        val numberingRulePatternMapSub = QNumberingRulePatternMapEntity.numberingRulePatternMapEntity
        return from(numberingRulePatternMap)
            .where(numberingRulePatternMap.numberingRule.numberingId.`in`(
                from(numberingRulePatternMapSub)
                    .select(numberingRulePatternMapSub.numberingRule.numberingId)
                    .where(numberingRulePatternMap.numberingPattern.patternId.`in`(patternList)
                        .and(numberingRulePatternMap.numberingRule.numberingId.ne(numberingId)))
            ))
            .fetchResults()
    }

}
