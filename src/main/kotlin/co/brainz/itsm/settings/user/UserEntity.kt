package co.brainz.itsm.settings.user

import org.springframework.format.annotation.DateTimeFormat
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "awfUser")
data class UserEntity(
        @Id @Column(name = "userId") val userId: String,
        @Column(name = "password") var password: String,
        @Column(name = "userName") var userName: String,
        @Column(name = "email") var email: String,
        @Column(name = "useYn") var useYn: Boolean,
        @Column(name = "tryLoginCount") var tryLoginCount: Int,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Column(name = "expiredDt") val expiredDt: LocalDateTime,
        @Column(name = "createId") val createId: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Column(name = "createDate") val createDate: LocalDateTime,
        @Column(name = "updateId") var updateId: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Column(name = "updateDate") var updateDate: LocalDateTime,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "awfUserRoleMap",
                joinColumns = [JoinColumn(name = "userId")],
                inverseJoinColumns = [JoinColumn(name = "roleId")])
        val roleEntities: Set<RoleEntity>?

) : Serializable