package co.brainz.itsm.numberingPattern.entity

import co.brainz.itsm.numberingRule.entity.NumberingRulePatternMapEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_numbering_pattern")
data class NumberingPatternEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "pattern_id", length = 128)
    val patternId: String,

    @Column(name = "pattern_name", length = 255)
    val patternName: String,

    @Column(name = "pattern_type", length = 100)
    val patternType: String,

    @Column(name = "pattern_value")
    val patternValue: String
) : Serializable {
    @OneToMany(mappedBy = "numberingPattern", fetch = FetchType.LAZY)
    val numberingRulePatternMapEntities = mutableListOf<NumberingRulePatternMapEntity>()
}
