/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.constants

object ApiConstants {

    const val CREATE_USER = "ADMIN"
    const val API_TOKEN_KEY = "Authorization"
    const val API_EXPIRES_IN = 300
    const val API_REFRESH_TOKEN_EXPIRES_IN = 86400

    enum class TokenType(val code: String) {
        ACCESS_TOKEN("access"),
        REFRESH_TOKEN("refresh")
    }
}
