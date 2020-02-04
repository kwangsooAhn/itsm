package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_url_auth_map")
@IdClass(AliceUrlAuthMapId::class)
data class AliceUrlAuthMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "url"),
        JoinColumn(name = "method")
    )
    val url: AliceUrlEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    val auth: AliceAuthEntity
) : Serializable

data class AliceUrlAuthMapId(val url: AliceUrlEntity, val auth: AliceAuthEntity) : Serializable {

}