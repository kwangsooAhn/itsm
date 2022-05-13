/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.entity

import co.brainz.itsm.sla.metricPool.entity.MetricPoolEntity
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
    var metric: MetricPoolEntity,

    @Id
    @Column(name = "metric_year", length = 128)
    var metricYear: String = "",

    @Column(name = "min_value")
    var minValue: Double? = null,

    @Column(name = "max_value")
    var maxValue: Double? = null,

    @Column(name = "weight_value")
    var weightValue: Double? = null,

    @Column(name = "owner", length = 100)
    var owner: String? = null,

    @Column(name = "comment")
    var note: String? = null,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null
) : Serializable

data class MetricYearEntityPk(
    val metric: String = "",
    val metricYear: String = ""
) : Serializable
