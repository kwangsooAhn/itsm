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
@Table(name = "cmdb_ci_group_list_data")
@IdClass(CIGroupListDataPk::class)
data class CIGroupListDataEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ci_id")
    val ci: CIEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    val ciAttribute: CIAttributeEntity,

    @Id
    @Column(name = "c_attribute_id")
    val cAttributeId: String = "",

    @Id
    @Column(name = "c_attribute_seq")
    val cAttributeSeq: Int = 0,

    @Column(name = "c_value")
    var cValue: String? = null
) : Serializable

data class CIGroupListDataPk(
    val ci: String = "",
    val ciAttribute: String = "",
    val cAttributeId: String = "",
    val cAttributeSeq: Int = 0
) : Serializable
