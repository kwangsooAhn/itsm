/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.entity

import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "sla_metric")
data class MetricPoolEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "metric_id", length = 128)
    var metricId: String = "",

    @Column(name = "metric_name", length = 100)
    var metricName: String = "",

    @Column(name = "metric_desc")
    var metricDesc: String? = null,

    @Column(name = "metric_group", length = 128)
    var metricGroup: String? = null,

    @Column(name = "metric_type", length = 128)
    var metricType: String? = null,

    @Column(name = "metric_unit", length = 128)
    var metricUnit: String? = null,

    @Column(name = "calculation_type", length = 128)
    var calculationType: String? = null,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null
) : Serializable {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "metric")
    var manual: MutableList<MetricManualEntity>? = mutableListOf()
}
