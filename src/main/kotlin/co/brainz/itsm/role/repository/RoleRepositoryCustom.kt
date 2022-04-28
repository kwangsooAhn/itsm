/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.role.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.itsm.role.dto.RoleSearchCondition

interface RoleRepositoryCustom : AliceRepositoryCustom {
    fun findRoleSearch(roleSearchCondition: RoleSearchCondition): PagingReturnDto

    fun findByRoleAll() : MutableList<RoleListDto>
}
