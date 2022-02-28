/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_dashboard_template")
data class DashboardTemplateEntity(
    @Id
    @Column(name = "template_id", length = 128)
    val templateId: String,

    @Column(name = "template_name", length = 128)
    val templateName: String,

    @Column(name = "template_config")
    val templateConfig: String? = null,

    @Column(name = "template_desc")
    val templateDesc: String? = null
) : Serializable
