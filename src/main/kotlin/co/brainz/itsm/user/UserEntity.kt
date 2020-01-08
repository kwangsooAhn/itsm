package co.brainz.itsm.user

import co.brainz.itsm.certification.constants.OAuthConstants
import co.brainz.itsm.role.RoleEntity
import org.hibernate.annotations.GenericGenerator
import org.springframework.format.annotation.DateTimeFormat
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
data class UserEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        val userKey: String,
        val userId: String,
        var password: String,
        var userName: String,
        var email: String,
        var useYn: Boolean = true,
        var tryLoginCount: Int = 0,
        var position: String? = null,
        var department: String? = null,
        var extensionNumber: String? = null,
        var createUserkey: String,
        var updateUserkey: String? = null,
        var status: String?,
        var certificationCode: String? = null,
        var platform: String? = OAuthConstants.PlatformEnum.ALICE.code,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var expiredDt: LocalDateTime? = null,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var createDt: LocalDateTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var updateDt: LocalDateTime? = null,
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "awfUserRoleMap",
                joinColumns = [JoinColumn(name = "userKey")],
                inverseJoinColumns = [JoinColumn(name = "roleId")])
        var roleEntities: Set<RoleEntity>?

) : Serializable
