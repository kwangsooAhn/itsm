/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.dto.UserSearchCondition
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl : QuerydslRepositorySupport(AliceUserEntity::class.java), UserRepositoryCustom {
    override fun findAliceUserEntityList(userSearchCondition: UserSearchCondition): QueryResults<UserListDataDto> {
        val user = QAliceUserEntity.aliceUserEntity
        val code = QCodeEntity.codeEntity
        val query = from(user)
            .select(
                Projections.constructor(
                    UserListDataDto::class.java,
                    user.userKey,
                    user.userId,
                    user.userName,
                    user.email,
                    user.position,
                    code.codeValue.`as`("department"),
                    user.officeNumber,
                    user.mobileNumber,
                    user.avatarType,
                    user.avatarValue,
                    Expressions.asString(""),
                    user.uploaded,
                    user.uploadedLocation,
                    user.createDt
                )
            )
            .leftJoin(code).on(code.code.eq(user.department))
            .where(
                super.like(user.userName, userSearchCondition.searchValue)
                    ?.or(super.like(user.userId, userSearchCondition.searchValue))
                    ?.or(super.like(user.position, userSearchCondition.searchValue))
                    ?.or(super.like(code.codeName, userSearchCondition.searchValue))
                    ?.or(super.like(user.officeNumber, userSearchCondition.searchValue))
                    ?.or(super.like(user.mobileNumber, userSearchCondition.searchValue))
            )
            .orderBy(user.userName.asc())

        if (userSearchCondition.isPaging) {
            query.limit(userSearchCondition.contentNumPerPage)
            query.offset((userSearchCondition.pageNum - 1) * userSearchCondition.contentNumPerPage)
        }

        return query.fetchResults()
    }
}
