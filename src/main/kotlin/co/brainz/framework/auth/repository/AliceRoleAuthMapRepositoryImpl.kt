/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceAuthSimpleDto
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.QAliceAuthEntity
import co.brainz.framework.auth.entity.QAliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.itsm.role.dto.RoleListDto
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceRoleAuthMapRepositoryImpl : QuerydslRepositorySupport(AliceRoleAuthMapEntity::class.java),
    AliceRoleAuthMapRepositoryCustom {

    override fun findAuthByRoles(roleIds: Set<String>): List<AliceAuthSimpleDto> {
        val auth = QAliceAuthEntity.aliceAuthEntity
        val authMap = QAliceRoleAuthMapEntity.aliceRoleAuthMapEntity
        val role = QAliceRoleEntity.aliceRoleEntity

        return from(auth)
            .select(
                Projections.constructor(
                    AliceAuthSimpleDto::class.java,
                    auth.authId,
                    auth.authName,
                    auth.authDesc
                )
            )
            .innerJoin(authMap).on(
                auth.eq(authMap.auth).and(
                    authMap.role.`in`(
                        JPAExpressions.select(role)
                            .from(role)
                            .where(role.roleId.`in`(roleIds))
                    )
                )
            )
            .orderBy(auth.authId.asc())
            .fetch()
    }

    override fun findRoleByAuths(auths: String): List<RoleListDto> {
        val auth = QAliceAuthEntity.aliceAuthEntity
        val authMap = QAliceRoleAuthMapEntity.aliceRoleAuthMapEntity
        val role = QAliceRoleEntity.aliceRoleEntity
        return from(role)
            .select(
                Projections.constructor(
                    RoleListDto::class.java,
                    role.roleId,
                    role.roleName,
                    role.roleDesc
                )
            )
            .innerJoin(authMap).on(role.eq(authMap.role).and(
                authMap.auth.`in`(JPAExpressions.select(auth)
                    .from(auth)
                    .where(auth.authId.`in`(auths)))
            ))
            .fetch()
    }
}
