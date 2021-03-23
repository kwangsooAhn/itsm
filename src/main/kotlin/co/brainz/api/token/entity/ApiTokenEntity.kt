/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.token.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_api_token")
data class ApiTokenEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "api_id", length = 128)
    var apiId: String,

    @Column(name = "access_token", length = 128)
    var accessToken: String,

    @Column(name = "expires_in")
    var expiresIn: Int,

    @Column(name = "refresh_token", length = 128)
    var refreshToken: String,

    @Column(name = "refresh_token_expires_in")
    var refreshTokenExpiresIn: Int,

    @Column(name = "create_dt")
    var createDt: LocalDateTime,

    @Column(name = "request_user_id", length = 128)
    var requestUserId: String? = null
)
