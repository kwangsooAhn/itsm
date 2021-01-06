package co.brainz.itsm.numberingRule.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_numbering_rule")
data class NumberingRuleEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "numbering_id", length = 128)
    val numberingId: String,

    @Column(name = "numbering_name", length = 255)
    val numberingName: String,

    @Column(name = "numbering_desc")
    val numberingDesc: String? = null,

    @Column(name = "latest_date")
    var latestDate: LocalDateTime? = null,

    @Column(name = "latest_value")
    var latestValue: String? = null
) : Serializable {
    @OneToMany(mappedBy = "numberingRule", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    val numberingRulePatternMapEntities = mutableListOf<NumberingRulePatternMapEntity>()
}
