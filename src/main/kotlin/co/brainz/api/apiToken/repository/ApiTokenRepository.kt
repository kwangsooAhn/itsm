/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.apiToken.repository

import co.brainz.api.apiToken.entity.ApiTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiTokenRepository : JpaRepository<ApiTokenEntity, String>, ApiTokenRepositoryCustom
