package co.brainz.framework.auth.entity

import co.brainz.framework.constants.UserConstants
import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.annotations.GenericGenerator
import org.springframework.format.annotation.DateTimeFormat
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_user")
data class AliceUserEntity(
        @Id @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(name = "user_key", length = 128)
        var userKey: String = "",

        @Column(name = "user_id", length = 128)
        var userId: String = "",

        @Column(name = "user_name", length = 128)
        var userName: String = "",

        @Column(name = "password", length = 1024)
        var password: String = "",

        @Column(name = "email", length = 1024)
        var email: String = "",

        @Column(name = "use_yn")
        var useYn: Boolean = true,

        @Column(name = "try_login_count")
        var tryLoginCount: Int = 0,

        @Column(name = "position", length = 128)
        var position: String? = null,

        @Column(name = "department", length = 128)
        var department: String? = null,

        @Column(name = "office_number", length = 128)
        var officeNumber: String? = null,

        @Column(name = "mobile_number", length = 128)
        var mobileNumber: String? = null,

        @Column(name = "status", length = 100)
        var status: String = UserConstants.Status.CERTIFIED.code,

        @Column(name = "certification_code", length = 128)
        var certificationCode: String? = null,

        @Column(name = "platform", length = 100)
        var platform: String = UserConstants.Platform.ALICE.code,

        @Column(name = "expired_dt")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        var expiredDt: LocalDateTime = LocalDateTime.now().plusMonths(3),

        @Column(name = "oauth_key", length = 256)
        var oauthKey: String? = "",

        @Column(name = "timezone", length = 100)
        var timezone: String = "",

        @Column(name = "lang", length = 100)
        var lang: String = "",

        @Column(name = "time_format", length = 100)
        var timeFormat: String = ""
): Serializable, AliceMetaEntity() {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val userRoleMapEntities = mutableListOf<AliceUserRoleMapEntity>()
}
