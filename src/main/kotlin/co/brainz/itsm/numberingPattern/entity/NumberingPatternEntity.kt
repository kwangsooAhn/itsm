package co.brainz.itsm.numberingPattern.entity

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_numbering_pattern_temp")
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
) : Serializable
