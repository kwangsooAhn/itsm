/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.entity

import co.brainz.itsm.numberingPattern.entity.NumberingPatternEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_rule_pattern_map")
@IdClass(NumberingRulePatternMapPk::class)
data class NumberingRulePatternMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numbering_id")
    val numberingRule: NumberingRuleEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pattern_id")
    val numberingPattern: NumberingPatternEntity,

    @Id
    @Column(name = "pattern_order")
    val patternOrder: Int
) : Serializable

data class NumberingRulePatternMapPk(
    val numberingRule: String = "",
    val numberingPattern: String = "",
    val patternOrder: Int = 0
) : Serializable
