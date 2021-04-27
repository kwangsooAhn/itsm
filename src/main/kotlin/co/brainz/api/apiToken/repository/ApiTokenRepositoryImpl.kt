/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.apiToken.repository

import co.brainz.api.apiToken.entity.ApiTokenEntity
import co.brainz.api.apiToken.entity.QApiTokenEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ApiTokenRepositoryImpl : QuerydslRepositorySupport(ApiTokenEntity::class.java), ApiTokenRepositoryCustom {

    override fun findAccessToken(accessToken: String): ApiTokenEntity? {
        val apiToken = QApiTokenEntity.apiTokenEntity
        return from(apiToken)
            .where(apiToken.accessToken.eq(accessToken))
            .orderBy(apiToken.createDt.desc())
            .fetchFirst()
    }

    override fun findRefreshToken(refreshToken: String): ApiTokenEntity? {
        val apiToken = QApiTokenEntity.apiTokenEntity
        return from(apiToken)
            .where(apiToken.refreshToken.eq(refreshToken))
            .orderBy(apiToken.createDt.desc())
            .fetchFirst()
    }
}
