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
        var createUserkey: String? = null,
        var createDt: LocalDateTime = LocalDateTime.now(),
        var updateUserkey: String? = null,
        var updateDt: LocalDateTime? = null,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        @JoinTable(name = "awfRoleAuthMap",
                joinColumns = [JoinColumn(name = "roleId", referencedColumnName = "roleId")],
                inverseJoinColumns = [JoinColumn(name = "authId", referencedColumnName = "authId")])
        var authEntityList: List<AliceAuthEntity>?
) : Serializable
