package co.brainz.itsm.settings.user

import org.springframework.format.annotation.DateTimeFormat
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awfUser")
data class UserEntity(
        @Id @Column(name = "userId") val userId: String,
        @Column(name = "password") val password: String,
        @Column(name = "userName") val userName: String,
        @Column(name = "email") val email: String,
        @Column(name = "useYn") val useYn: Boolean,
        @Column(name = "tryLoginCount") val tryLoginCount: Int,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Column(name = "expiredDt") val expiredDt: LocalDateTime,
        @Column(name = "createId") val createId: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Column(name = "createDate") val createDate: LocalDateTime,
        @Column(name = "updateId") val updateId: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Column(name = "updateDate") val updateDate: LocalDateTime
) : Serializable