package co.brainz.itsm.user

import co.brainz.itsm.role.RoleEntity
import co.brainz.itsm.certification.ServiceTypeEnum
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
        var useYn: Boolean = true,
        var tryLoginCount: Int = 0,
        var position: String? = null,
        var department: String? = null,
        var extensionNumber: String? = null,
        var createUserid: String,
        var updateUserid: String? = null,
        var status: String?,
        var certificationCode: String? = null,
        var serviceType: String? = ServiceTypeEnum.ALICE.code,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var expiredDt: LocalDateTime? = null,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var createDt: LocalDateTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var updateDt: LocalDateTime? = null,
        @ManyToMany(fetch = FetchType.LZAY)
        @JoinTable(name = "awfUserRoleMap",
                joinColumns = [JoinColumn(name = "userId")],
                inverseJoinColumns = [JoinColumn(name = "roleId")])
        var roleEntities: Set<RoleEntity>?

) : Serializable