package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_user_role_map")
data class AliceUserRoleMapEntity(
        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @Column(name = "user_key")
        val user: AliceUserEntity,

        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @Column(name = "role_id")
        val role: AliceRoleEntity
) : Serializable {
}