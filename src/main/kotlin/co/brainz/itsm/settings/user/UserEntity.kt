package co.brainz.itsm.settings.user

import org.springframework.format.annotation.DateTimeFormat
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "awf_user")
data class UserEntity(
        @Id val userId: String,
        var password: String,
        var userName: String,
        var email: String,
        var useYn: Boolean,
        var tryLoginCount: Int,
        var position: String?,
        var department: String?,
        var extensionNumber: String?,
        var createUserid: String,
        var updateUserid: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var expiredDt: LocalDateTime?,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var createDt: LocalDateTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var updateDt: LocalDateTime?,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "awfUserRoleMap",
                joinColumns = [JoinColumn(name = "userId")],
                inverseJoinColumns = [JoinColumn(name = "roleId")])
        var roleEntities: Set<RoleEntity>?

) : Serializable