package co.brainz.itsm.sla.metricYear.entity

import co.brainz.itsm.sla.metricPool.entity.MetricEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "sla_metric_year")
@IdClass(MetricYearEntityPk::class)
data class MetricYearEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id")
    var metric: MetricEntity,

    @Id
    @Column(name = "metric_year")
    var metricYear: String = "",

    @Column(name = "min_value")
    var minValue: String? = null,

    @Column(name = "max_value")
    var maxValue: String? = null,

    @Column(name = "weight_value")
    var weightValue: String? = null,

    @Column(name = "owner")
    var owner: String? = null,

    @Column(name = "note")
    var note: String? = null,

    @Column(name = "create_user_key")
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key")
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null
) : Serializable

data class MetricYearEntityPk(
    val metric: String = "",
    val metricYear: String = ""
) : Serializable
