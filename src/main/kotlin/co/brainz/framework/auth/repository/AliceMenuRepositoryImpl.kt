/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceMenuDto
import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.framework.auth.entity.QAliceMenuAuthMapEntity
import co.brainz.framework.auth.entity.QAliceMenuEntity
import co.brainz.framework.auth.entity.QAliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.QAliceUserRoleMapEntity
import co.brainz.framework.organization.entity.QOrganizationRoleMapEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class AliceMenuRepositoryImpl : QuerydslRepositorySupport(AliceMenuEntity::class.java), AliceMenuRepositoryCustom {
    override fun findMenuByUserKey(userKey: String): Set<AliceMenuDto> {
        val menu = QAliceMenuEntity.aliceMenuEntity
        val menuAuthMap = QAliceMenuAuthMapEntity.aliceMenuAuthMapEntity
        val roleAuthMap = QAliceRoleAuthMapEntity.aliceRoleAuthMapEntity
        val userRoleMap = QAliceUserRoleMapEntity.aliceUserRoleMapEntity

        val query = from(menu)
            .select(
                Projections.constructor(
                    AliceMenuDto::class.java,
                    menu.menuId,
                    menu.pMenuId,
                    menu.url,
                    menu.sort,
                    menu.useYn
                )
            )
            .innerJoin(menuAuthMap).on(menuAuthMap.menu.menuId.eq(menu.menuId))
            .innerJoin(roleAuthMap).on(roleAuthMap.auth.authId.eq(menuAuthMap.auth.authId))
            .innerJoin(userRoleMap).on(userRoleMap.role.roleId.eq(roleAuthMap.role.roleId))
            .where(
                userRoleMap.user.userKey.eq(userKey)
            )
            .orderBy(menu.sort.asc())

        return query.fetch().toSet()
    }

    override fun findMenuByGroupId(groupId: String?): Set<AliceMenuDto> {
        val menu = QAliceMenuEntity.aliceMenuEntity
        val menuAuthMap = QAliceMenuAuthMapEntity.aliceMenuAuthMapEntity
        val roleAuthMap = QAliceRoleAuthMapEntity.aliceRoleAuthMapEntity
        val organizationRoleMap = QOrganizationRoleMapEntity.organizationRoleMapEntity

        val query = from(menu)
            .select(
                Projections.constructor(
                    AliceMenuDto::class.java,
                    menu.menuId,
                    menu.pMenuId,
                    menu.url,
                    menu.sort,
                    menu.useYn
                )
            )
            .innerJoin(menuAuthMap).on(menuAuthMap.menu.menuId.eq(menu.menuId))
            .innerJoin(roleAuthMap).on(roleAuthMap.auth.authId.eq(menuAuthMap.auth.authId))
            .innerJoin(organizationRoleMap).on(organizationRoleMap.role.roleId.eq(roleAuthMap.role.roleId))
            .where(
                organizationRoleMap.organization.organizationId.eq(groupId)
            )
            .orderBy(menu.sort.asc())

        return query.fetch().toSet()
    }
}
