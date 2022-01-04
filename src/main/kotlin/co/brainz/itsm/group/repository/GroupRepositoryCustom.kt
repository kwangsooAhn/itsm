/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.group.dto.GroupSearchCondition
import co.brainz.itsm.group.dto.PGroupDto
import co.brainz.itsm.group.entity.GroupEntity
import com.querydsl.core.QueryResults

interface GroupRepositoryCustom : AliceRepositoryCustom {

    fun findByGroupSearchList(searchValue: String): QueryResults<GroupEntity>

    fun findGroupList(groupSearchCondition: GroupSearchCondition): QueryResults<PGroupDto>
}
