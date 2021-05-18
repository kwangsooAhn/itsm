/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.component.entity

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
@Table(name = "wf_component_property")
@IdClass(WfComponentPropertyPk::class)
data class WfComponentPropertyEntity(
    @Id
    @Column(name = "component_id", length = 128)
    val componentId: String,

    @Id
    @Column(name = "property_type", length = 100)
    val propertyType: String,

    @Column(name = "property_options")
    val propertyOptions: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "component_id", nullable = false, insertable = false, updatable = false)
    val properties: WfComponentEntity

) : Serializable

data class WfComponentPropertyPk(
    val componentId: String = "",
    val propertyType: String = ""
) : Serializable
