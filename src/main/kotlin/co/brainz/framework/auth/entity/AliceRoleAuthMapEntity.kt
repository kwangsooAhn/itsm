package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_role_auth_map")
@IdClass(AliceRoleAuthMapPk::class)
data class AliceRoleAuthMapEntity(
        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "role_id")
        val role: AliceRoleEntity,

        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "auth_id")
        val auth: AliceAuthEntity
) : Serializable

data class AliceRoleAuthMapPk(
        val role: String,
        val auth: String
) : Serializable
