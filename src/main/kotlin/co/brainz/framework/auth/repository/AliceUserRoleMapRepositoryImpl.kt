/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserRoleMapEntity
import co.brainz.itsm.role.dto.RoleListDto
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceUserRoleMapRepositoryImpl : QuerydslRepositorySupport(AliceUserRoleMapEntity::class.java),
    AliceUserRoleMapRepositoryCustom {

    override fun findUserRoleByUserKey(userKey: String): MutableList<RoleListDto> {
        val role = QAliceRoleEntity.aliceRoleEntity
        val roleMap = QAliceUserRoleMapEntity.aliceUserRoleMapEntity
        val user = QAliceUserEntity.aliceUserEntity
        return from(role)
            .select(
                Projections.constructor(
                    RoleListDto::class.java,
                    role.roleId,
                    role.roleName,
                    role.roleDesc
                )
            )
            .innerJoin(roleMap).on(
                role.eq(roleMap.role).and(
                    roleMap.user.eq(
                        JPAExpressions.select(user)
                            .from(user)
                            .where(user.userKey.eq(userKey))
                    )
                )
            )
            .orderBy(role.roleId.asc())
            .fetch()
    }

    override fun findUserRoleMapByRoleId(roleId: String): MutableList<AliceUserRoleMapEntity> {
        val roleMap = QAliceUserRoleMapEntity.aliceUserRoleMapEntity
        val role = QAliceRoleEntity.aliceRoleEntity
        return from(roleMap)
            .innerJoin(role).on(roleMap.role.eq(role))
            .fetchJoin()
            .where(role.roleId.eq(roleId))
            .fetch()
    }

    override fun findUserRoleMapByRoleIds(roleIds: MutableList<String>): MutableList<AliceUserRoleMapEntity> {
        val roleMap = QAliceUserRoleMapEntity.aliceUserRoleMapEntity
        val role = QAliceRoleEntity.aliceRoleEntity
        return from(roleMap)
            .innerJoin(role).on(roleMap.role.eq(role))
            .fetchJoin()
            .where(role.roleId.`in`(roleIds))
            .fetch()
    }
}
