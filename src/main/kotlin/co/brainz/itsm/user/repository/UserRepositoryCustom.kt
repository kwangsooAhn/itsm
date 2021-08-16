/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.user.dto.UserListReturnDto
import co.brainz.itsm.user.dto.UserSearchCondition

interface UserRepositoryCustom : AliceRepositoryCustom {
    fun findAliceUserEntityList(userSearchCondition: UserSearchCondition): UserListReturnDto
}
