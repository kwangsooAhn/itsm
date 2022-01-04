/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.repository

import co.brainz.itsm.group.dto.GroupSearchCondition
import co.brainz.itsm.group.dto.PGroupDto
import co.brainz.itsm.group.entity.GroupEntity
import co.brainz.itsm.group.entity.QGroupEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class GroupRepositoryImpl : QuerydslRepositorySupport(GroupEntity::class.java), GroupRepositoryCustom {

    override fun findByGroupSearchList(searchValue: String): QueryResults<GroupEntity> {
        val group = QGroupEntity.groupEntity

        return from(group)
            .select(group)
            .where(super.likeIgnoreCase(group.groupName, searchValue))
            .orderBy(group.level.asc(),group.seqNum.asc())
            .fetchResults()
    }

    override fun findGroupList(groupSearchCondition: GroupSearchCondition): QueryResults<PGroupDto> {
        val group = QGroupEntity.groupEntity
        val query = from(group)
            .select(
                Projections.constructor(
                    PGroupDto::class.java,
                    group.groupId,
                    group.pGroup.groupId,
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
}
