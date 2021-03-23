/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.token.repository

import co.brainz.api.token.entity.ApiTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiTokenRepository : JpaRepository<ApiTokenEntity, String>, ApiTokenRepositoryCustom
