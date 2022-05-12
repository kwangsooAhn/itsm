package co.brainz.itsm.sla.metricManual.entity

import co.brainz.itsm.sla.metricPool.entity.MetricEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "sla_metric_manual")
data class MetricManualEntity(
    @Id
    @Column(name = "metric_manual_id")
    var metricManualId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id")
    var metric: MetricEntity,

    @Column(name = "reference_dt")
    var referenceDt: LocalDateTime,

    @Column(name = "metric_value")
    var metricValue: Double = 0.0,

    @Column(name = "create_user_key")
    var userKey: String? = "",

    @Column(name = "create_dt")
    var createDt: LocalDateTime? = null
) : Serializable

