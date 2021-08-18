/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.dto.UserSearchCondition
import com.querydsl.core.QueryResults

interface UserRepositoryCustom : AliceRepositoryCustom {
    fun findAliceUserEntityList(userSearchCondition: UserSearchCondition): QueryResults<UserListDataDto>
}
