/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.role.dto.RoleListDto

interface GroupRoleMapRepositoryCustom : AliceRepositoryCustom {

    fun findGroupUseRoleByGroupId(groupId: String): MutableList<RoleListDto>
}
