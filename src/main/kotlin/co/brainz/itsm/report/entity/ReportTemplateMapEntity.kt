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
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@IdClass(ReportTemplateMapPk::class)
@Table(name = "awf_report_template_map")
data class ReportTemplateMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    val template: ReportTemplateEntity,

    @Id
    @Column(name = "chart_id")
    val chartId: String,

    @Column(name = "display_order")
    val displayOrder: Int
) : Serializable

data class ReportTemplateMapPk(
    val template: String = "",
    val chartId: String = ""
) : Serializable
