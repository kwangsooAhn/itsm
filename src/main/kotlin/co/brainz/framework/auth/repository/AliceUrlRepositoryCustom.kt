/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceUrlDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface AliceUrlRepositoryCustom : AliceRepositoryCustom {
    fun findUrlByUserKey(userKey: String): Set<AliceUrlDto>
    fun findUrlByGroupId(groupId: String?): Set<AliceUrlDto>
}
