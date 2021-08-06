/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_report_template")
data class ReportTemplateEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "template_id")
    val templateId: String = "",

    @Column(name = "template_name", length = 128)
    var templateName: String = "",

    @Column(name = "template_desc", length = 512)
    var templateDesc: String?,

    @Column(name = "automatic")
    var automatic: Boolean = false

) : Serializable, AliceMetaEntity() {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
    val report: MutableList<ReportEntity>? = mutableListOf()
}
