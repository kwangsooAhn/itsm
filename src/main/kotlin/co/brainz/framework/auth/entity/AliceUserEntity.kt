/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.entity

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.user.entity.UserCustomEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
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
open class AliceUserEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "user_key", length = 128)
    open var userKey: String = "",

    @Column(name = "user_id", length = 128)
    open var userId: String = "",

    @Column(name = "user_name", length = 128)
    open var userName: String = "",

    @Column(name = "password", length = 1024)
    open var password: String = "",

    @Column(name = "email", length = 1024)
    open var email: String = "",

    @Column(name = "use_yn")
    open var useYn: Boolean = true,

    @Column(name = "try_login_count")
    open var tryLoginCount: Int = 0,

    @Column(name = "position", length = 128)
    open var position: String? = "",

    @Column(name = "department", length = 128)
    open var department: String? = "",

    @Column(name = "office_number", length = 128)
    open var officeNumber: String? = "",

    @Column(name = "mobile_number", length = 128)
    open var mobileNumber: String? = "",

    @Column(name = "status", length = 100)
    open var status: String = AliceUserConstants.Status.CERTIFIED.code,

    @Column(name = "certification_code", length = 128)
    open var certificationCode: String? = null,

    @Column(name = "platform", length = 100)
    open var platform: String = AliceUserConstants.Platform.ALICE.code,

    @Column(name = "expired_dt")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var expiredDt: LocalDateTime = LocalDateTime.now()
        .plusMonths(AliceConstants.EXPIRED_MONTH_PERIOD.toLong()),

    @Column(name = "oauth_key", length = 256)
    open var oauthKey: String? = "",

    @Column(name = "timezone", length = 100)
    open var timezone: String = "",

    @Column(name = "lang", length = 100)
    open var lang: String = "",

    @Column(name = "time_format", length = 100)
    open var timeFormat: String = "",

    @Column(name = "theme", length = 100)
    open var theme: String = "",

    @Column(name = "avatar_type", length = 100)
    open var avatarType: String = AliceUserConstants.AvatarType.FILE.code,

    @Column(name = "avatar_value", length = 512)
    open var avatarValue: String = AliceUserConstants.AVATAR_BASIC_FILE_NAME,

    @Column(name = "uploaded")
    open var uploaded: Boolean = false,

    @Column(name = "uploaded_location")
    open var uploadedLocation: String = "",

    @CreatedBy
    @Column(name = "create_user_key", nullable = false, updatable = false)
    open var createUser: String? = null,

    @CreatedDate
    @Column(name = "create_dt", nullable = false, updatable = false)
    open var createDt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedBy
    @Column(name = "update_user_key", insertable = false)
    open var updateUser: String? = null,

    @LastModifiedDate
    @Column(name = "update_dt", insertable = false)
    open var updateDt: LocalDateTime? = LocalDateTime.now()

) : Serializable {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    open val userRoleMapEntities = mutableListOf<AliceUserRoleMapEntity>()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    open val userCustomEntities = mutableListOf<UserCustomEntity>()
}
