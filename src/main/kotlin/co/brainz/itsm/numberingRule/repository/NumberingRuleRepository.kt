/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.repository

import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NumberingRuleRepository : JpaRepository<NumberingRuleEntity, String>, NumberingRuleRepositoryCustom
