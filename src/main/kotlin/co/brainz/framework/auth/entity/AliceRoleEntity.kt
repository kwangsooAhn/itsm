package co.brainz.framework.auth.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_role")
data class AliceRoleEntity(
        @Id var roleId: String,
        var roleName: String,
        var roleDesc: String,
        var createUserkey: String?,
        var createDt: LocalDateTime?,
        var updateUserkey: String?,
        var updateDt: LocalDateTime?,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "awfRoleAuthMap",
                joinColumns = [JoinColumn(name = "roleId", referencedColumnName = "roleId")],
                inverseJoinColumns = [JoinColumn(name = "authId", referencedColumnName = "authId")])
        var aliceAuthEntities: MutableSet<AliceAuthEntity> = HashSet()
) : Serializable
