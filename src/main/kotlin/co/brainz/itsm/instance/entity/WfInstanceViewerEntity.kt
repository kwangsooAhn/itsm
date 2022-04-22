/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "wf_instance_viewer")
@IdClass(WfInstanceViewerEntityPk::class)
data class WfInstanceViewerEntity(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
    val instance: WfInstanceEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_key")
    var viewer: AliceUserEntity,

    @Column(name = "review_yn")
    var reviewYn: Boolean = false,

    @Column(name = "display_yn")
    var displayYn: Boolean = false,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null

) : Serializable

data class WfInstanceViewerEntityPk(
    val instance: String = "",
    val viewer: String = ""
) : Serializable
