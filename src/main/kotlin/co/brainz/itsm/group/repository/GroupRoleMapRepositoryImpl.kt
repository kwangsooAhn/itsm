package co.brainz.itsm.group.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.itsm.group.entity.GroupEntity
import co.brainz.itsm.group.entity.GroupRoleMapEntity
import co.brainz.itsm.group.entity.QGroupRoleMapEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class GroupRoleMapRepositoryImpl : QuerydslRepositorySupport(GroupRoleMapEntity::class.java),
    GroupRoleMapRepositoryCustom {

    override fun findGroupRoleMapByGroupId(groupId: String): MutableList<AliceRoleEntity> {
        val role = QAliceRoleEntity.aliceRoleEntity
        val groupRoleMap = QGroupRoleMapEntity.groupRoleMapEntity

        return from(groupRoleMap)
            .select(
                Projections.constructor(
                    AliceRoleEntity::class.java,
                    role.roleId,
                    role.roleName,
                    role.roleDesc
                )
            )
            .innerJoin(role).on(groupRoleMap.roleId.eq(role))
            .where(groupRoleMap.groupId.eq(GroupEntity(groupId)))
            .fetch()

    }
}

