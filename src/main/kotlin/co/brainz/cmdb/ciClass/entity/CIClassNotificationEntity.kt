/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
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
@Table(name = "cmdb_class_notification")
@IdClass(CIClassNotificationPk::class)
data class CIClassNotificationEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    val ciClass: CIClassEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", referencedColumnName = "attribute_id")
    val ciAttribute: CIAttributeEntity,

    @Column(name = "attribute_order")
    val attributeOrder: Int,

    @Id
    @Column(name = "condition")
    val condition: String,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_attribute_id", referencedColumnName = "attribute_id")
    val ciTargetAttribute: CIAttributeEntity
) : Serializable

data class CIClassNotificationPk(
    val ciClass: String = "",
    val ciAttribute: String = "",
    val condition: String = "",
    val ciTargetAttribute: String = ""
) : Serializable
