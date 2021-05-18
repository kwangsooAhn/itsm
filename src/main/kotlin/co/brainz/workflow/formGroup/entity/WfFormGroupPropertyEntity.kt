/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.formGroup.entity

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
@Table(name = "wf_form_group_property")
@IdClass(WfFormGroupPropertyPk::class)
data class WfFormGroupPropertyEntity(
    @Id
    @Column(name = "form_group_id", length = 128)
    val formGroupId: String,

    @Id
    @Column(name = "property_type", length = 100)
    val propertyType: String,

    @Column(name = "property_options")
    val propertyOptions: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "form_group_id", nullable = false, insertable = false, updatable = false)
    val properties: WfFormGroupEntity

) : Serializable

data class WfFormGroupPropertyPk(
    val formGroupId: String = "",
    val propertyType: String = ""
) : Serializable
