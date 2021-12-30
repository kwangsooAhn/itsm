/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.dto.UserListExcelDto
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
                super.likeIgnoreCase(user.userName, userSearchCondition.searchValue)
                    ?.or(super.likeIgnoreCase(user.userId, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.position, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(code.codeName, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.officeNumber, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.mobileNumber, userSearchCondition.searchValue))
            )
            .where(
                user.userName.notIn(AliceUserConstants.CREATE_USER_ID)
            )
        if (userSearchCondition.excludeIds.isNotEmpty()) {
            query.where(user.userKey.notIn(userSearchCondition.excludeIds))
        }
        if (userSearchCondition.isFilterUseYn) {
            query.where(user.useYn.eq(true))
        }
        query.orderBy(user.userName.asc())

        if (userSearchCondition.isPaging) {
            query.limit(userSearchCondition.contentNumPerPage)
            query.offset((userSearchCondition.pageNum - 1) * userSearchCondition.contentNumPerPage)
        }

        return query.fetchResults()
    }

    override fun findUserListForExcel(userSearchCondition: UserSearchCondition): QueryResults<UserListExcelDto> {
        val user = QAliceUserEntity.aliceUserEntity
        val code = QCodeEntity.codeEntity
        val query = from(user)
            .select(
                Projections.constructor(
                    UserListExcelDto::class.java,
                    user.userId,
                    user.userName,
                    user.email,
                    code.codeValue.`as`("department"),
                    user.position,
                    user.officeNumber,
                    user.mobileNumber,
                    user.createDt,
                    user.absenceYn,
                    user.useYn
                )
            )

            .leftJoin(code).on(code.code.eq(user.department))
            .where(
                super.likeIgnoreCase(user.userName, userSearchCondition.searchValue)
                    ?.or(super.likeIgnoreCase(user.userId, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.position, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(code.codeName, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.officeNumber, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.mobileNumber, userSearchCondition.searchValue))
            )
            .where(
                user.userName.notIn(AliceUserConstants.CREATE_USER_ID)
            )
            .orderBy(user.userName.asc())

        return query.fetchResults()
    }
}
