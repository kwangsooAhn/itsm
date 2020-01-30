package co.brainz.itsm.user.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="awf_timezone")
data class UserTimezoneEntity(
        @Id @Column(name="timezone_id") val timezoneId: String,
        @Column(name="timezone_value") val timezoneValue: String
) : Serializable