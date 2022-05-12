/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "sla_metric_group")
data class MetricGroupEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "metric_group_id", length = 128)
    var metricGroupId: String = "",

    @Column(name = "metric_group_name")
    var metricGroupName: String = "",

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null
) : Serializable
