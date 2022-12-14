/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.user.dto.UserCustomDto

interface UserCustomRepositoryCustom : AliceRepositoryCustom {
    fun findByUserAndCustomType(user: AliceUserEntity, customType: String): UserCustomDto?
    fun findByCustomType(customType: String): List<UserCustomDto>?
}
