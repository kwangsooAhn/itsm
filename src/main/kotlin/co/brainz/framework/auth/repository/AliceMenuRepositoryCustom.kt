/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceMenuDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface AliceMenuRepositoryCustom : AliceRepositoryCustom {
    fun findMenuByUserKey(userKey: String): Set<AliceMenuDto>
    fun findMenuByGroupId(groupId: String?): Set<AliceMenuDto>
}
