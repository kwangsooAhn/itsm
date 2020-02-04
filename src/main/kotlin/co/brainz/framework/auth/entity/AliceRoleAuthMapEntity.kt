package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_role_auth_map")
data class AliceRoleAuthMapEntity(
        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @Column(name = "role_id")
        val role: AliceRoleEntity,

        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @Column(name = "auth_id")
        val auth: AliceAuthEntity
) : Serializable {
}