/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceUrlDto
import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.QAliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.QAliceUrlAuthMapEntity
import co.brainz.framework.auth.entity.QAliceUrlEntity
import co.brainz.framework.auth.entity.QAliceUserRoleMapEntity
import co.brainz.itsm.group.entity.QGroupRoleMapEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class AliceUrlRepositoryImpl : QuerydslRepositorySupport(AliceUrlEntity::class.java), AliceUrlRepositoryCustom {
    override fun findUrlByUserKey(userKey: String): Set<AliceUrlDto> {
        val url = QAliceUrlEntity.aliceUrlEntity
        val urlAuthMap = QAliceUrlAuthMapEntity.aliceUrlAuthMapEntity
        val roleAuthMap = QAliceRoleAuthMapEntity.aliceRoleAuthMapEntity
        val userRoleMap = QAliceUserRoleMapEntity.aliceUserRoleMapEntity

        val query = from(url)
            .select(
                Projections.constructor(
                    AliceUrlDto::class.java,
                    url.url,
                    url.method,
                    url.urlDesc,
                    url.requiredAuth
                )
            )
            .innerJoin(urlAuthMap).on(urlAuthMap.url.eq(url))
            .innerJoin(roleAuthMap).on(roleAuthMap.auth.authId.eq(urlAuthMap.auth.authId))
            .innerJoin(userRoleMap).on(userRoleMap.role.roleId.eq(roleAuthMap.role.roleId))
            .where(
                userRoleMap.user.userKey.eq(userKey)
            )

        return query.fetch().toSet()
    }

    override fun findUrlByGroupId(groupId: String?): Set<AliceUrlDto> {
        val url = QAliceUrlEntity.aliceUrlEntity
        val urlAuthMap = QAliceUrlAuthMapEntity.aliceUrlAuthMapEntity
        val roleAuthMap = QAliceRoleAuthMapEntity.aliceRoleAuthMapEntity
        val groupRoleMap = QGroupRoleMapEntity.groupRoleMapEntity

        val query = from(url)
            .select(
                Projections.constructor(
                    AliceUrlDto::class.java,
                    url.url,
                    url.method,
                    url.urlDesc,
                    url.requiredAuth
                )
            )
            .innerJoin(urlAuthMap).on(urlAuthMap.url.eq(url))
            .innerJoin(roleAuthMap).on(roleAuthMap.auth.authId.eq(urlAuthMap.auth.authId))
            .innerJoin(groupRoleMap).on(groupRoleMap.role.roleId.eq(roleAuthMap.role.roleId))
            .where(
                groupRoleMap.group.groupId.eq(groupId)
            )
        return query.fetch().toSet()
    }
}
