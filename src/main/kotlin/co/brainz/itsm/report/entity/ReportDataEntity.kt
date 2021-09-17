/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.entity

import java.io.Serializable
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
@Table(name = "awf_report_data")
data class ReportDataEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "data_id")
    val dataId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    var report: ReportEntity,

    @Column(name = "chart_id", length = 128)
    val chartId: String? = null,

    @Column(name = "display_order")
    val displayOrder: Int,

    @Column(name = "values")
    val values: String? = null
) : Serializable
