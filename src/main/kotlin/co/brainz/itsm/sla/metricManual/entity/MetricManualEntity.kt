/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.entity

import co.brainz.itsm.sla.metricPool.entity.MetricPoolEntity
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "sla_metric_manual")
data class MetricManualEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "metric_manual_id", length = 128)
    var metricManualId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id")
    var metric: MetricPoolEntity,

    @Column(name = "reference_dt")
    var referenceDate: LocalDate? = null,

    @Column(name = "metric_value")
    var metricValue: Float? = null,

    @Column(name = "create_user_key", length = 128)
    var userKey: String? = "",

    @Column(name = "create_dt")
    var createDt: LocalDateTime? = null
) : Serializable
