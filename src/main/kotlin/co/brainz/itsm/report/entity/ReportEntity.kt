/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.entity

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
@Table(name = "awf_report")
data class ReportEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "report_id")
    val reportId: String = "",

    @Column(name = "template_id")
    var templateId: String = "",

    @Column(name = "report_name", length = 128)
    val reportName: String = "",

    @Column(name = "report_desc", length = 512)
    val reportDesc: String?,

    @Column(name = "publish_dt")
    val publishDt: LocalDateTime? = null
) : Serializable {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    val data: MutableList<ReportDataEntity>? = mutableListOf()
}
