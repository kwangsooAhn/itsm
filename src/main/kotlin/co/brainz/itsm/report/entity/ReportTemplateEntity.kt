/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
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

    @Column(name = "report_name", length = 128)
    var reportName: String?,

    @Column(name = "automatic")
    var automatic: Boolean = false,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_key", referencedColumnName = "user_key", nullable = false, updatable = false)
    var createUser: AliceUserEntity? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_user_key", referencedColumnName = "user_key")
    var updateUser: AliceUserEntity? = null

) : Serializable {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
    val charts: List<ReportTemplateMapEntity>? = mutableListOf()
}
