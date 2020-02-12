package co.brainz.framework.auth.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_role")
data class AliceRoleEntity(
        @Id
        @Column(name = "role_id", length = 100)
        val roleId: String,

        @Column(name = "role_name", length = 128)
        val roleName: String,

        @Column(name = "role_desc")
        val roleDesc: String

): Serializable, AliceMetaEntity() {
        @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
        val roleAuthMapEntities = mutableListOf<AliceRoleAuthMapEntity>()
}
