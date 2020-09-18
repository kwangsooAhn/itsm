package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class UserRepositoryImpl : QuerydslRepositorySupport(AliceUserEntity::class.java),
    UserRepositoryCustom {

    override fun findAliceUserEntityList(
        search: String,
        category: String,
        offset: Long
    ): QueryResults<AliceUserEntity> {
        val user = QAliceUserEntity.aliceUserEntity
        val query = from(user)
            .where(
                user.userName.containsIgnoreCase(search).or(user.userId.containsIgnoreCase(search))
                    .or(user.position.containsIgnoreCase(search)).or(user.department.containsIgnoreCase(search))
                    .or(user.officeNumber.containsIgnoreCase(search))
            )
        if (category != "all") {
            query.where(user.platform.eq(category))
        }
        query.orderBy(user.userName.asc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)

        return query.fetchResults()
    }
}
