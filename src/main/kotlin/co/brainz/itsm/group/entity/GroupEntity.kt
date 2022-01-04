/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction

@Entity
@Table(name = "awf_group")
data class GroupEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "group_id", length = 100)
    var groupId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_group_id")
    @NotFound(action = NotFoundAction.IGNORE)
    var pGroupId: GroupEntity? = null,

    @Column(name = "group_name", length = 128)
    var groupName: String? = null,

    @Column(name = "group_desc", length = 128)
    var groupDesc: String? = null,

    @Column(name = "use_yn")
    var useYn: Boolean? = true,

    @Column(name = "level")
    var level: Int? = null,

    @Column(name = "seq_num")
    var seqNum: Int? = null,

    @Column(name = "editable")
    var editable: Boolean? = true,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null

) : Serializable {
    @OneToMany(mappedBy = "groupId", fetch = FetchType.LAZY)
    val groupRoleMapEntities = mutableListOf<GroupRoleMapEntity>()
}