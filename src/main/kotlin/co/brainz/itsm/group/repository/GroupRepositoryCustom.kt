package co.brainz.itsm.group.repository

import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.group.dto.GroupShortDto
import co.brainz.itsm.group.dto.PGroupListDto
import com.querydsl.core.QueryResults

interface GroupRepositoryCustom: AliceRepositoryCustom {

    fun findGroupList(searchDto: SearchDto): QueryResults<PGroupListDto>

    fun findGroupDetail(search: String): GroupShortDto

}