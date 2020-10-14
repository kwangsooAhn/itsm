/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.entity

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.format.annotation.DateTimeFormat

@Entity
@Table(name = "awf_user")
data class AliceUserEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
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
    var position: String? = "",

    @Column(name = "department", length = 128)
    var department: String? = "",

    @Column(name = "office_number", length = 128)
    var officeNumber: String? = "",

    @Column(name = "mobile_number", length = 128)
    var mobileNumber: String? = "",

    @Column(name = "status", length = 100)
    var status: String = AliceUserConstants.Status.CERTIFIED.code,

    @Column(name = "certification_code", length = 128)
    var certificationCode: String? = null,

    @Column(name = "platform", length = 100)
    var platform: String = AliceUserConstants.Platform.ALICE.code,

    @Column(name = "expired_dt")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var expiredDt: LocalDateTime = LocalDateTime.now()
        .plusMonths(AliceConstants.EXPIRED_MONTH_PERIOD.toLong()),

    @Column(name = "oauth_key", length = 256)
    var oauthKey: String? = "",

    @Column(name = "timezone", length = 100)
    var timezone: String = "",

    @Column(name = "lang", length = 100)
    var lang: String = "",

    @Column(name = "time_format", length = 100)
    var timeFormat: String = "",

    @Column(name = "theme", length = 100)
    var theme: String = "",

    @Column(name = "avatar_type", length = 100)
    var avatarType: String = AliceUserConstants.AvatarType.FILE.code,

    @Column(name = "avatar_value", length = 512)
    var avatarValue: String = AliceUserConstants.AVATAR_BASIC_FILE_NAME,

    @Column(name = "uploaded")
    var uploaded: Boolean = false,

    @Column(name = "uploaded_location")
    var uploadedLocation: String = "",

    @CreatedBy
    @Column(name = "create_user_key", nullable = false, updatable = false)
    var createUser: String? = null,

    @CreatedDate
    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedBy
    @Column(name = "update_user_key", insertable = false)
    var updateUser: String? = null,

    @LastModifiedDate
    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = LocalDateTime.now()

) : Serializable {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val userRoleMapEntities = mutableListOf<AliceUserRoleMapEntity>()
}
