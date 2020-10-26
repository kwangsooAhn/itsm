/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.user.dto.UserListDto

interface UserRepositoryCustom : AliceRepositoryCustom {
    fun findAliceUserEntityList(search: String, offset: Long): MutableList<UserListDto>
}
