package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.dto.UserCustomDto
import co.brainz.itsm.user.entity.QUserCustomEntity
import co.brainz.itsm.user.entity.UserCustomEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class UserCustomRepositoryImpl : QuerydslRepositorySupport(UserCustomEntity::class.java), UserCustomRepositoryCustom {
    override fun findByUserAndCustomType(user: AliceUserEntity, customType: String): UserCustomDto? {
        val userCustom = QUserCustomEntity.userCustomEntity
        val query = from(userCustom)
            .select(
                Projections.constructor(
                    UserCustomDto::class.java,
                    userCustom.user.userKey,
                    userCustom.customType,
                    userCustom.customValue
                )
            )
            .where(userCustom.user.userKey.eq(user.userKey).and(userCustom.customType.eq(customType)))
        return query.fetchOne()
    }

    override fun findByCustomType(customType: String): List<UserCustomDto>? {
        val userCustom = QUserCustomEntity.userCustomEntity
        val query = from(userCustom)
            .select(
                Projections.constructor(
                    UserCustomDto::class.java,
                    userCustom.user.userKey,
                    userCustom.customType,
                    userCustom.customValue
                )
            )
            .where(userCustom.customType.eq(customType))
        return query.fetch()
    }
}
