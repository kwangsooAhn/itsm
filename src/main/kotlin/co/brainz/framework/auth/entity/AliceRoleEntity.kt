package co.brainz.framework.auth.entity

import co.brainz.framework.auditor.AliceMetaEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_role")
data class AliceRoleEntity(
        @Id var roleId: String,
        var roleName: String,
        var roleDesc: String
        /*
        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        @JoinTable(name = "awfRoleAuthMap",
                joinColumns = [JoinColumn(name = "roleId", referencedColumnName = "roleId")],
                inverseJoinColumns = [JoinColumn(name = "authId", referencedColumnName = "authId")])
        var authEntityList: List<AliceAuthEntity>?
        */
): Serializable, AliceMetaEntity() {
        @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
        val userRoleMapEntities = mutableListOf<AliceUserRoleMapEntity>()

        @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
        val roleAuthMapEntities = mutableListOf<AliceRoleAuthMapEntity>()
}