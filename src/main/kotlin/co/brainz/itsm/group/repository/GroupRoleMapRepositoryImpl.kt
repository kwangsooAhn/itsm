package co.brainz.itsm.group.repository

import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.itsm.group.entity.GroupEntity
import co.brainz.itsm.group.entity.GroupRoleMapEntity
import co.brainz.itsm.group.entity.QGroupRoleMapEntity
import co.brainz.itsm.role.dto.RoleListDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class GroupRoleMapRepositoryImpl : QuerydslRepositorySupport(GroupRoleMapEntity::class.java),
    GroupRoleMapRepositoryCustom {

    override fun findGroupUseRoleByGroupId(groupId: String): MutableList<RoleListDto> {
        val role = QAliceRoleEntity.aliceRoleEntity
        val groupRoleMap = QGroupRoleMapEntity.groupRoleMapEntity

        return from(groupRoleMap)
            .select(
                Projections.constructor(
                    RoleListDto::class.java,
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

