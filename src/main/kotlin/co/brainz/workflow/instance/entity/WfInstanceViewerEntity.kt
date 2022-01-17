/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instance.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.folder.entity.WfFolderEntity
import co.brainz.itsm.instance.entity.WfCommentEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "wf_instance_viewer")
data class WfInstanceViewerEntity(

    @Id
    @Column(name = "instance_id", length = 128)
    val instanceId: String,

    @Id
    @Column(name = "viewer_key", length = 128)
    var viewerKey: String,

    @Column(name = "review_yn")
    val reviewYn: Boolean? = false,

    @Column(name = "display_yn")
    var displayYn: Boolean? = false,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null

) : Serializable
