package co.brainz.itsm.numbering.entity

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.Table

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

        @Column(name = "latest_value")
        var latestValue: String? = null

) : Serializable {
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "numberingRule", cascade = [CascadeType.REMOVE])
        @OrderBy("pattern_order asc")
        val patterns: MutableList<NumberingPatternEntity>? = mutableListOf()
}
