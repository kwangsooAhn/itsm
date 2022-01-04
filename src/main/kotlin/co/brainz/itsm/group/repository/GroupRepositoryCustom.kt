package co.brainz.itsm.group.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.group.dto.GroupSearchCondition
import co.brainz.itsm.group.dto.GroupShortDto
import co.brainz.itsm.group.dto.PGroupDto
import com.querydsl.core.QueryResults

interface GroupRepositoryCustom : AliceRepositoryCustom {
    fun findGroupList(groupSearchCondition: GroupSearchCondition): QueryResults<PGroupDto>

    fun findGroupDetail(search: String): GroupShortDto
}