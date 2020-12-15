package co.brainz.itsm.numberingRule.repository

import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingRuleRepositoryImpl : QuerydslRepositorySupport(NumberingRuleEntity::class.java), NumberingRuleRepositoryCustom {
}
