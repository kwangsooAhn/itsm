package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.framework.auth.entity.QAliceUserRoleMapEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceUserRoleMapRepositoryImpl : QuerydslRepositorySupport(AliceUserRoleMapEntity::class.java), AliceUserRoleMapRepositoryCustom {
    override fun findUserRoleByUserKey(userKey: String): MutableList<AliceRoleEntity> {
        val roleMap = QAliceUserRoleMapEntity.aliceUserRoleMapEntity
        val role = QAliceRoleEntity.aliceRoleEntity
        return from(role)
            .innerJoin(roleMap).on(role.eq(roleMap.role))
            .fetchJoin()
            .where(roleMap.user.userKey.eq(userKey))
            .fetch()
    }


}
