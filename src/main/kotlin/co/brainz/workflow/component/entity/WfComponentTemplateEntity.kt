/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "wf_component_template")
data class WfComponentTemplateEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "template_id", length = 128)
    val templateId: String,

    @Column(name = "template_name", length = 128)
    val templateName: String,

    @Column(name = "component_type", length = 100)
    val componentType: String,

    @Column(name = "component_data")
    val componentData: String
) : Serializable
