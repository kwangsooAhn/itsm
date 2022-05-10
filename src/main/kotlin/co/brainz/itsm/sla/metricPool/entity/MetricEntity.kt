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
@Table(name = "sla_metric")
data class MetricEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "metric_id")
    var metricId: String = "",

    @Column(name = "metric_name")
    var metricName: String = "",

    @Column(name = "metric_desc")
    var metricDesc: String? = null,

    @Column(name = "metric_group_id")
    var metricGroupId: String = "",

    @Column(name = "metric_type")
    var metricType: String? = null,

    @Column(name = "metric_unit")
    var metricUnit: String? = null,

    @Column(name = "calculation_type")
    var calculationType: String? = null,

    @Column(name = "create_user_key")
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key")
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null
) : Serializable
