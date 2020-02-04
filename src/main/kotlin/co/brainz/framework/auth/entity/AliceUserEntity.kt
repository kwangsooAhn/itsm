package co.brainz.framework.auth.entity

import co.brainz.framework.constants.UserConstants
import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.annotations.GenericGenerator
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_user")
data class AliceUserEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        val userKey: String,
        var userId: String,
        var userName: String,
        var password: String,
        var email: String,
        val useYn: Boolean = true,
        val tryLoginCount: Int = 0,
        var position: String? = null,
        var department: String? = null,
        var officeNumber: String? = null,
        var mobileNumber: String? = null,
        override var createUserkey: String = UserConstants.CREATE_USER_ID,
        var status: String = UserConstants.Status.CERTIFIED.code,
        var certificationCode: String? = null,
        var platform: String = UserConstants.Platform.ALICE.code,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val expiredDt: LocalDateTime,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "awf_user_role_map",
                joinColumns = [JoinColumn(name = "user_key")],
                inverseJoinColumns = [JoinColumn(name = "role_id")]
        )
        val roleEntities: List<AliceRoleEntity>,

        //@ManyToMany(fetch = FetchType.EAGER)
        //@JoinTable(name = "awfUserRoleMap",
        //           joinColumns = [JoinColumn(name = "userKey")],
        //           inverseJoinColumns = [JoinColumn(name = "roleId")])
        //var roleEntities: Set<AliceRoleEntity>?,
        var oauthKey: String?,
        var timezone: String,
        var lang: String

): Serializable, AliceMetaEntity() {
    fun getAuthorities(): MutableSet<GrantedAuthority> {

        val authorities = mutableSetOf<GrantedAuthority>()
        val rolePrefix = "ROLE_"
        for (role in this.roleEntities!!) {
            authorities.add(SimpleGrantedAuthority(rolePrefix + role.roleId))
            for (auth in role.authEntityList!!) {
                authorities.add(SimpleGrantedAuthority(auth.authId))
            }
        }

        return authorities
    }
}
