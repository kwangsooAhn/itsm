package co.brainz.itsm.sla.slaManual.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "sla_metric_manual")
data class MetricManualEntity(
    @Id
    @Column(name = "metric_id")
    var metricId: String = "",

    @Id
    @Column(name = "reference_dt")
    var referenceDt: LocalDateTime,

    @Id
    @Column(name = "metric_value")
    var metricValue: Long = 0L,

    @Column(name = "create_user_key")
    var userKey: String? = "",

    @Column(name = "create_dt")
    var createDt: LocalDateTime? = null
)
