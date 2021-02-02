/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.entity

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
@Table(name = "cmdb_ci_data")
@IdClass(CIDataPk::class)
data class CIDataEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ci_id")
    val ci: CIEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    val ciAttribute: CIAttributeEntity,

    @Column(name = "value")
    var value: String? = null
) : Serializable

data class CIDataPk(
    val ci: String = "",
    val ciAttribute: String = ""
) : Serializable
