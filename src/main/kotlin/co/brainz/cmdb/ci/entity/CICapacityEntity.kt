/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.entity

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
@Table(name = "cmdb_ci_capacity_data")
@IdClass(CapacityPk::class)
data class CICapacityEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ci_id")
    val ci: CIEntity,

    @Id
    @Column(name = "reference_dt")
    val referenceDt: LocalDateTime,

    @Column(name = "cpu_avg")
    val cpuAvg: Double? = 0.0,

    @Column(name = "memory_avg")
    val memAvg: Double? = 0.0,

    @Column(name = "disk_avg")
    val diskAvg: Double? = 0.0,

    @Column(name = "mapping_id")
    val mappingId: String? = ""
) : Serializable

data class CapacityPk(
    val ci: String = "",
    val referenceDt: LocalDateTime
) : Serializable
