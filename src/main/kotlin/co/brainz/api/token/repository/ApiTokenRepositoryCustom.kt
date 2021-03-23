/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.token.repository

import co.brainz.api.token.entity.ApiTokenEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface ApiTokenRepositoryCustom : AliceRepositoryCustom {

    fun findAccessToken(accessToken: String): ApiTokenEntity?
    fun findRefreshToken(refreshToken: String): ApiTokenEntity?
}
