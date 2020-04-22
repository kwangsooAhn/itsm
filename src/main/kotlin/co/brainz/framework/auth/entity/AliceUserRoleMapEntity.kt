package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_user_role_map")
@IdClass(AliceUserRoleMapPk::class)
data class AliceUserRoleMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    val user: AliceUserEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    val role: AliceRoleEntity
) : Serializable

data class AliceUserRoleMapPk(
    val user: String = "",
    val role: String = ""
) : Serializable
