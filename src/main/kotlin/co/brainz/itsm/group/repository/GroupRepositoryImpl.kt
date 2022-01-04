package co.brainz.itsm.group.repository

import co.brainz.itsm.group.dto.GroupShortDto
import co.brainz.itsm.group.dto.GroupSearchCondition
import co.brainz.itsm.group.dto.PGroupDto
import co.brainz.itsm.group.entity.QGroupEntity
import co.brainz.itsm.group.entity.GroupEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class GroupRepositoryImpl : QuerydslRepositorySupport(GroupEntity::class.java), GroupRepositoryCustom {

    override fun findGroupList(groupSearchCondition: GroupSearchCondition): QueryResults<PGroupDto> {
        val group = QGroupEntity.groupEntity
        val query = from(group)
            .select(
                Projections.constructor(
                    PGroupDto::class.java,
                    group.groupId,
                    group.pGroupId,
                    group.groupName,
                    group.groupDesc
                )
            )
            .where(
                super.likeIgnoreCase(group.groupName, groupSearchCondition.searchValue)
            )
        if (groupSearchCondition.isPaging) {
            query.limit(groupSearchCondition.contentNumPerPage)
            query.offset((groupSearchCondition.pageNum - 1) * groupSearchCondition.contentNumPerPage)
        }
        return query.fetchResults()
    }

    override fun findGroupDetail(search: String): GroupShortDto {
        val group = QGroupEntity.groupEntity
        return from(group)
            .select(
                Projections.constructor(
                    GroupShortDto::class.java,
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

