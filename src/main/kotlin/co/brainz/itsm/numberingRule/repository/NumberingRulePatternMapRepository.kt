package co.brainz.itsm.numberingRule.repository

import co.brainz.itsm.numberingRule.entity.NumberingRulePatternMapEntity
import co.brainz.itsm.numberingRule.entity.NumberingRulePatternMapPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NumberingRulePatternMapRepository : JpaRepository<NumberingRulePatternMapEntity, NumberingRulePatternMapPk>
