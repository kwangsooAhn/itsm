/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.entity

import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "cmdb_ci_instance_relation")
@IdClass(CIInstanceRelationPk::class)
data class CIInstanceRelationEntity(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ci_id")
    val ci: CIEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
    val instance: WfInstanceEntity
) : Serializable

data class CIInstanceRelationPk(
    val ci: String = "",
    val instance: String = ""
) : Serializable
