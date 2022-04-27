/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.itsm.role.dto.RoleSearchCondition
import com.querydsl.core.QueryResults
import org.springframework.data.domain.Page

interface RoleRepositoryCustom : AliceRepositoryCustom {
    fun findRoleSearch(roleSearchCondition: RoleSearchCondition): Page<RoleListDto>

    fun findByRoleAll() : MutableList<RoleListDto>
}
