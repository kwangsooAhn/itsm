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
@IdClass(GroupRoleMapPk::class)
data class GroupRoleMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    val group: GroupEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    val role: AliceRoleEntity
) : Serializable

data class GroupRoleMapPk(
    val group: String = "",
    val role: String = ""
) : Serializable