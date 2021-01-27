/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.entity

import co.brainz.cmdb.ciAttribute.entity.CIAttributeEntity
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
@Table(name = "cmdb_class_attribute_map")
@IdClass(CIClassAttributeMapPk::class)
data class CIClassAttributeMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    val ciClass: CIClassEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    val ciAttribute: CIAttributeEntity,

    @Column(name = "attribute_order")
    val attributeOrder: Int
) : Serializable

data class CIClassAttributeMapPk(
    val ciClass: String = "",
    val ciAttribute: String = ""
) : Serializable
