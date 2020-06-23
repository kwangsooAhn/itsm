package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class UserRepositoryImpl : QuerydslRepositorySupport(AliceUserEntity::class.java),
    UserRepositoryCustom {

    override fun findAliceUserEntityList(search: String, category: String): List<AliceUserEntity> {
        val user = QAliceUserEntity.aliceUserEntity

        val query = from(user)
            .innerJoin(user.createUser).fetchJoin()
            .innerJoin(user.updateUser).fetchJoin()
            .where(user.userName.containsIgnoreCase(search).or(user.userId.containsIgnoreCase(search)))
        if (category != "all") {
            query.where(user.platform.eq(category))
        }
        query.orderBy(user.userName.asc())

        return query.fetch()
    }
}
