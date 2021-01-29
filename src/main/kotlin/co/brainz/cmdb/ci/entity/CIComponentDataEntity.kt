/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "wf_component_ci_data")
@IdClass(CIComponentDataPk::class)
data class CIComponentDataEntity(

    @Id
    @Column(name = "ci_id", length = 128)
    val ciId: String,

    @Id
    @Column(name = "component_id", length = 128)
    val componentId: String,

    @Column(name = "values")
    var values: String,

    @Column(name = "instance_id", length = 128)
    var instanceId: String? = null

) : Serializable

data class CIComponentDataPk(
    val ciId: String = "",
    val componentId: String = ""
) : Serializable
