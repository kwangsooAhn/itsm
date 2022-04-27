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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingRuleRepositoryImpl : QuerydslRepositorySupport(NumberingRuleEntity::class.java),
    NumberingRuleRepositoryCustom {

    override fun findRuleSearch(numberingRuleSearchCondition: NumberingRuleSearchCondition): Page<NumberingRuleListDto> {
        val rule = QNumberingRuleEntity.numberingRuleEntity
        val pageable = Pageable.unpaged()
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
                super.likeIgnoreCase(rule.numberingName, numberingRuleSearchCondition.searchValue)
            )
            .orderBy(rule.numberingName.desc())
            val totalCount = query.fetch().size

        query.limit(numberingRuleSearchCondition.contentNumPerPage)
            .offset((numberingRuleSearchCondition.pageNum - 1) * numberingRuleSearchCondition.contentNumPerPage)
        return PageImpl<NumberingRuleListDto>(query.fetch(), pageable, totalCount.toLong())
    }
}
