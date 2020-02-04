package co.brainz.framework.auth.entity

import co.brainz.framework.menu.entity.AliceMenuEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_menu_auth_map")
data class AliceMenuAuthMapEntity (
        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @Column(name = "menu_id")
        val menu: AliceMenuEntity,

        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @Column(name = "auth_id")
        val auth: AliceAuthEntity
) : Serializable{
}