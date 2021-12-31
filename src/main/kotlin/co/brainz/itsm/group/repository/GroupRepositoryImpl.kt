package co.brainz.itsm.group.repository

import co.brainz.itsm.group.entity.GroupEntity
import co.brainz.itsm.group.entity.QGroupEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class GroupRepositoryImpl : QuerydslRepositorySupport(GroupEntity::class.java), GroupRepositoryCustom {
    override fun findByGroupList(): QueryResults<GroupEntity> {
        val group = QGroupEntity.groupEntity

        return from(group)
            .orderBy(group.level.asc(),group.seqNum.asc())
            .fetchResults()
    }

    override fun findByGroupSearchList(searchValue: String): QueryResults<GroupEntity> {
        val group = QGroupEntity.groupEntity

        return from(group)
            .select(group)
            .where(super.likeIgnoreCase(group.groupName, searchValue))
            .orderBy(group.level.asc(),group.seqNum.asc())
            .fetchResults()
    }
}