package co.brainz.itsm.settings.user

import org.springframework.format.annotation.DateTimeFormat
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import co.brainz.itsm.role.entity.RoleEntity

@Entity
@Table(name = "awfUser")
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
        var createId: String,
        var updateId: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var expiredDt: LocalDateTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var createDate: LocalDateTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var updateDate: LocalDateTime,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "awfUserRoleMap",
                joinColumns = [JoinColumn(name = "userId")],
                inverseJoinColumns = [JoinColumn(name = "roleId")])
        var roleEntities: Set<RoleEntity>?

) : Serializable