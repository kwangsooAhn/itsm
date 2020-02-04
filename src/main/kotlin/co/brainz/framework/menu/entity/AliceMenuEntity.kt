package co.brainz.framework.menu.entity

import co.brainz.framework.auth.entity.AliceAuthEntity
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_menu")
data class AliceMenuEntity(
        @Id
        val menuId: String,
        val pMenuId: String,
        val url: String,
        val sort: Int,
        val useYn: Boolean,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "awf_menu_auth_map",
                joinColumns = [JoinColumn(name = "menu_id")],
                inverseJoinColumns = [JoinColumn(name = "auth_id")])
        val authEntities: List<AliceAuthEntity>

): Serializable {
}
