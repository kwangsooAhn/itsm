package co.brainz.itsm.group.entity
/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction

@Entity
@Table(name = "awf_group")
data class GroupEntity (
    @Id @Column(name = "group_id", length = 100)
    var groupId: String = "",

    @JoinColumn(name = "p_group_id")
    var pGroupId: String ="",

    @Column(name = "group_name", length = 128)
    var groupName: String? = null,

    @Column(name = "group_desc", length = 256)
    var groupDesc: String? = null,

    @Column(name = "use_yn")
    var useYn: Boolean? = true,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt")
    var createDt: Date? = true,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @Column(name = "update_dt")
    var updateDt: Int? = null

) : Serializable, AliceMetaEntity()