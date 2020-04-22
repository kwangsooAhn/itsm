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
@Table(name = "awf_auth")
data class AliceAuthEntity(
    @Id
    @Column(name = "auth_id", length = 100)
    val authId: String,

    @Column(name = "auth_name", length = 128)
    val authName: String,

    @Column(name = "auth_desc")
    val authDesc: String

) : Serializable, AliceMetaEntity() {
    @OneToMany(mappedBy = "auth", fetch = FetchType.LAZY)
    val urlAuthMapEntities = mutableListOf<AliceUrlAuthMapEntity>()

    @OneToMany(mappedBy = "auth", fetch = FetchType.LAZY)
    val menuAuthMapEntities = mutableListOf<AliceMenuAuthMapEntity>()
}
