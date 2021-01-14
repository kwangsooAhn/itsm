/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.entity

import co.brainz.cmdb.ciAttribute.entity.CmdbAttributeEntity
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
@IdClass(CmdbClassAttributeMapPk::class)
data class CmdbClassAttributeMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    val cmdbClass: CmdbClassEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    val cmdbAttribute: CmdbAttributeEntity,

    @Column(name = "attribute_order")
    val attriibuteOrder: Int
) : Serializable

data class CmdbClassAttributeMapPk(
    val cmdbClass: String = "",
    val cmdbAttribute: String = ""
) : Serializable
