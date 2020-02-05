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

        //@ManyToMany(fetch = FetchType.EAGER)
        //@JoinTable(name = "awfUserRoleMap",
        //           joinColumns = [JoinColumn(name = "userKey")],
        //           inverseJoinColumns = [JoinColumn(name = "roleId")])
        //var roleEntities: Set<AliceRoleEntity>?,
        var oauthKey: String?,
        var timezone: String,
        var lang: String

): Serializable, AliceMetaEntity() {

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val userRoleMapEntities = mutableListOf<AliceUserRoleMapEntity>()

    fun getAuthorities(): MutableSet<GrantedAuthority> {

        val authorities = mutableSetOf<GrantedAuthority>()
        val rolePrefix = "ROLE_"
        this.userRoleMapEntities.forEach{userRoleMap ->
            authorities.add(SimpleGrantedAuthority(rolePrefix + userRoleMap.role.roleId))
            userRoleMap.role.roleAuthMapEntities.forEach{roleAuthMap ->
                authorities.add(SimpleGrantedAuthority(roleAuthMap.auth.authId))
            }
        }

        return authorities
    }
}
