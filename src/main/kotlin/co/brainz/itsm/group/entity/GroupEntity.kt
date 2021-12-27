package co.brainz.itsm.group.entity

import co.brainz.framework.auth.entity.AliceUserEntity
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
@Table(name = "awf_group")
data class GroupEntity(
    @Id
    @Column(name = "group_id", length = 100)
    val groupId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_group_id")
    val pGroupId: GroupEntity? = null,

    @Column(name = "group_name", length = 128)
    val groupName: String? = null,

    @Column(name = "group_desc", length = 128)
    val groupDesc: String? = null,

    @Column(name = "use_yn")
    val useYn: Boolean? = true,

    @Column(name = "level")
    val level: Int? = null,

    @Column(name = "seq_num")
    val seqNum: Int? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    val createDt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_key", referencedColumnName = "user_key")
    val createUser: AliceUserEntity? = null,

    @Column(name = "update_dt", insertable = false)
    val updateDt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_user_key", referencedColumnName = "user_key")
    val updateUser: AliceUserEntity? = null
) : Serializable {
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    val groupRoleMapEntities = mutableListOf<GroupRoleMapEntity>()
}
