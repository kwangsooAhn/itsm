package co.brainz.framework.auth.entity

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
import javax.persistence.Table

@Entity
@Table(name = "awf_user")
data class AliceUserEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        val userKey: String,
        val userId: String,
        val userName: String,
        val password: String,
        val email: String,
        val useYn: Boolean,
        val tryLoginCount: Int,
        val createUserkey: String?,
        val updateUserkey: String?,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val expiredDt: LocalDateTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val createDt: LocalDateTime?,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val updateDt: LocalDateTime?,
        val platform: String = "",

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "awfUserRoleMap",
                joinColumns = [JoinColumn(name = "userKey")],
                inverseJoinColumns = [JoinColumn(name = "roleId")])
        val aliceRoleEntities: Set<AliceRoleEntity>?

) : Serializable {
    fun getAuthorities(): MutableSet<GrantedAuthority> {

        val authorities = mutableSetOf<GrantedAuthority>()
        if (this.aliceRoleEntities != null) {
            val rolePrefix = "ROLE_"
            for (role in this.aliceRoleEntities) {
                authorities.add(SimpleGrantedAuthority(rolePrefix + role.roleId))
                for (auth in role.aliceAuthEntities) {
                    authorities.add(SimpleGrantedAuthority(auth.authId))
                }
            }
        }

        return authorities
    }
}
