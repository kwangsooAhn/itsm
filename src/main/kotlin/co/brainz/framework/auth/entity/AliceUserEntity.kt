package co.brainz.framework.auth.entity

import co.brainz.framework.constants.AliceConstants
import org.hibernate.annotations.GenericGenerator
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_user")
data class AliceUserEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        val userKey: String,
        val userId: String,
        var userName: String,
        val password: String,
        var email: String,
        val useYn: Boolean = true,
        val tryLoginCount: Int = 0,
        var position: String? = null,
        var department: String? = null,
        var extensionNumber: String? = null,
        val createUserkey: String?,
        var updateUserkey: String? = null,
        var status: String = AliceConstants.UserEnum.Status.CERTIFIED.code,
        var certificationCode: String? = null,
        var platform: String = AliceConstants.UserEnum.Platform.ALICE.code,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val expiredDt: LocalDateTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val createDt: LocalDateTime?,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        var updateDt: LocalDateTime? = null,
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "awfUserRoleMap",
                joinColumns = [JoinColumn(name = "userKey")],
                inverseJoinColumns = [JoinColumn(name = "roleId")])
        var roleEntities: Set<AliceRoleEntity>?

) : Serializable {
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
