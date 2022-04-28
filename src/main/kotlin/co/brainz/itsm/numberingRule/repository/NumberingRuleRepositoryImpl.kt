/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleListDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleSearchCondition
import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.itsm.numberingRule.entity.QNumberingRuleEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingRuleRepositoryImpl : QuerydslRepositorySupport(NumberingRuleEntity::class.java),
    NumberingRuleRepositoryCustom {

    override fun findRuleSearch(numberingRuleSearchCondition: NumberingRuleSearchCondition): PagingReturnDto {
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
            .where(builder(numberingRuleSearchCondition, rule))
            .orderBy(rule.numberingName.desc())
            .limit(numberingRuleSearchCondition.contentNumPerPage)
            .offset((numberingRuleSearchCondition.pageNum - 1) * numberingRuleSearchCondition.contentNumPerPage)

        val countQuery = from(rule)
            .select(rule.count())
            .where(builder(numberingRuleSearchCondition, rule))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }
    private fun builder(numberingRuleSearchCondition: NumberingRuleSearchCondition, rule: QNumberingRuleEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(super.likeIgnoreCase(rule.numberingName, numberingRuleSearchCondition.searchValue))
        return builder
    }
}
