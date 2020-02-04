package co.brainz.framework.auth.entity

import co.brainz.framework.menu.entity.AliceMenuEntity
import co.brainz.framework.auditor.AliceMetaEntity
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
@Table(name = "awf_auth")
data class AliceAuthEntity(
        @Id var authId: String,
        var authName: String,
        var authDesc: String,
        /*@ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "awfMenuAuthMap",
                   joinColumns = [JoinColumn(name = "authId")],
                   inverseJoinColumns = [JoinColumn(name = "menuId")])
        val aliceMenuList: Set<AliceMenuEntity>,*/

        @OneToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "awf_menu_auth_map",
                joinColumns = [JoinColumn(name = "auth_id")],
                inverseJoinColumns = [JoinColumn(name = "menu_id")])
        val menuEntities: List<AliceMenuEntity>,

        /*@ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "awf_url_auth_map",
                   joinColumns = [JoinColumn(name = "authId")],
                   inverseJoinColumns = [JoinColumn(name = "url", referencedColumnName = "url"),
                                         JoinColumn(name = "method", referencedColumnName = "method")])
        val aliceUrl: Set<AliceUrlEntity>*/

        @OneToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "awf_url_auth_map",
                joinColumns = [JoinColumn(name = "auth_id")],
                inverseJoinColumns = [JoinColumn(name = "url"), JoinColumn(name = "method")])
        val urlEntities: List<AliceUrlEntity>,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "awf_role_auth_map",
                joinColumns = [JoinColumn(name = "auth_id")],
                inverseJoinColumns = [JoinColumn(name = "role_id")])
        val roleEntities: List<AliceRoleEntity>
): Serializable, AliceMetaEntity()