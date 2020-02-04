package co.brainz.framework.auth.entity

import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.mapping.Join
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_role")
data class AliceRoleEntity(
        @Id var roleId: String,
        var roleName: String,
        var roleDesc: String,
        /*
        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        @JoinTable(name = "awfRoleAuthMap",
                joinColumns = [JoinColumn(name = "roleId", referencedColumnName = "roleId")],
                inverseJoinColumns = [JoinColumn(name = "authId", referencedColumnName = "authId")])
        var authEntityList: List<AliceAuthEntity>?
        */

        @OneToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "awf_user_role_map",
                joinColumns = [JoinColumn(name = "role_id")],
                inverseJoinColumns = [JoinColumn(name = "user_id")]
        )
        val userEntities: List<AliceUserEntity>

): Serializable, AliceMetaEntity()