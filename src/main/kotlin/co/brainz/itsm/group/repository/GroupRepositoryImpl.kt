package co.brainz.itsm.group.repository

import co.brainz.cmdb.dto.SearchDto
import co.brainz.itsm.group.dto.GroupDetailDto
import co.brainz.itsm.group.dto.GroupDetailReturnDto
import co.brainz.itsm.group.dto.PGroupListDto
import co.brainz.itsm.group.entity.GroupEntity
import co.brainz.itsm.group.entity.QGroupEntity
import co.brainz.itsm.role.dto.RoleListDto
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class GroupRepositoryImpl : QuerydslRepositorySupport(GroupEntity::class.java), GroupRepositoryCustom {

    override fun findGroupList(searchDto: SearchDto): QueryResults<PGroupListDto> {
        val group = QGroupEntity.groupEntity
        val query = from(group)
            .select(
                Projections.constructor(
                    PGroupListDto::class.java,
                    group.groupId,
                    group.pGroupId,
                    group.groupName,
                    group.groupDesc
                )
            )
            .where(
                super.likeIgnoreCase(group.groupName, searchDto.search)
            )
        if (searchDto.limit != null) {
            query.limit(searchDto.limit)
        }
        if (searchDto.offset != null) {
            query.offset(searchDto.offset)
        }
        return query.fetchResults()
    }

    override fun findGroupDetail(search: String): GroupDetailDto {
        val group = QGroupEntity.groupEntity
        return from(group)
            .select(
                Projections.constructor(
                    GroupDetailDto::class.java,
                    group.groupId,
                    group.pGroupId,
                    group.groupName,
                    group.groupDesc,
                    group.useYn
                )
            )
            .where(group.groupId.eq(search))
            .fetchOne()
    }
}

