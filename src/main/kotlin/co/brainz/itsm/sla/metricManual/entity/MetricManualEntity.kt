/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricManual.entity

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
@Table(name = "sla_metric_manual")
@IdClass(MetricManualPk::class)
data class MetricManualEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id")
    val metric: MetricEntity,

    @Id
    @Column(name = "reference_dt")
    val referenceDt: LocalDateTime? = null,

    @Id
    @Column(name = "metric_value")
    val metricValue: Int = 0,

    @Column(name = "create_user_key")
    var userKey: String? = "",

    @Column(name = "create_dt")
    var createDt: LocalDateTime? = null
) : Serializable

data class MetricManualPk(
    val metric: String = "",
    val referenceDt: LocalDateTime? = null,
    val metricValue: Int = 0
) : Serializable
