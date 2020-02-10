package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="awf_timezone")
data class TimezoneEntity(
        @Id
        @Column(name="timezone_id", length = 128)
        val timezoneId: String,

        @Column(name="timezone_value", length = 128)
        val timezoneValue: String
) : Serializable
