package co.brainz.itsm.group.entity

import co.brainz.framework.auth.entity.AliceRoleEntity
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_group_role_map")
@IdClass(GroupRoleMapEntityPk::class)
data class GroupRoleMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    var groupId: GroupEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    var roleId: AliceRoleEntity
) : Serializable

data class GroupRoleMapEntityPk(
    val groupId: String = "",
    val roleId: String = ""
) : Serializable