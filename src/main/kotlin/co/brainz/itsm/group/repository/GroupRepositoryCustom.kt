package co.brainz.itsm.group.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.group.entity.GroupEntity
import com.querydsl.core.QueryResults

interface GroupRepositoryCustom : AliceRepositoryCustom {

    fun findByGroupAll(): QueryResults<GroupEntity>

    fun findByGroupList(searchValue: String): QueryResults<GroupEntity>

}
