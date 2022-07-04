/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.organization.entity.QOrganizationEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.dto.UserListExcelDto
import co.brainz.itsm.user.dto.UserSearchCondition
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl : QuerydslRepositorySupport(AliceUserEntity::class.java), UserRepositoryCustom {
    val user: QAliceUserEntity = QAliceUserEntity.aliceUserEntity
    val organization: QOrganizationEntity = QOrganizationEntity.organizationEntity

    override fun findAliceUserEntityList(userSearchCondition: UserSearchCondition): PagingReturnDto {
        val query = from(user)
            .select(
                Projections.constructor(
                    UserListDataDto::class.java,
                    user.userKey,
                    user.userId,
                    user.userName,
                    user.email,
                    user.position,
                    organization.organizationId,
                    organization.organizationName,
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
            .leftJoin(organization).on(organization.organizationId.eq(user.department))
            .where(builder(userSearchCondition))
        query.orderBy(user.userName.asc())
        if (userSearchCondition.isPaging) {
            query.limit(userSearchCondition.contentNumPerPage)
            query.offset((userSearchCondition.pageNum - 1) * userSearchCondition.contentNumPerPage)
        }

        val countQuery = from(user)
            .select(user.count())
            .leftJoin(organization).on(organization.organizationId.eq(user.department))
            .where(builder(userSearchCondition))
        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    override fun findUserListForExcel(userSearchCondition: UserSearchCondition): List<UserListExcelDto> {
        val query = from(user)
            .select(
                Projections.constructor(
                    UserListExcelDto::class.java,
                    user.userId,
                    user.userName,
                    user.email,
                    user.position,
                    organization.organizationName,
                    user.officeNumber,
                    user.mobileNumber,
                    user.createDt,
                    user.absenceYn,
                    user.useYn
                )
            )
            .leftJoin(organization).on(organization.organizationId.eq(user.department))
            .where(
                super.likeIgnoreCase(user.userName, userSearchCondition.searchValue)
                    ?.or(super.likeIgnoreCase(user.userId, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.position, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(organization.organizationName, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.officeNumber, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.mobileNumber, userSearchCondition.searchValue))
            )
            .where(
                user.userName.notIn(UserConstants.CREATE_USER_ID)
            )
            .orderBy(user.userName.asc())

        return query.fetch()
    }

    override fun getUserListInOrganization(organizationIds: Set<String>): List<AliceUserEntity> {
        return from(user)
            .where(user.department.`in`(organizationIds))
            .fetch()
    }

    private fun builder(userSearchCondition: UserSearchCondition): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(user.userName, userSearchCondition.searchValue)
                ?.or(super.likeIgnoreCase(user.userId, userSearchCondition.searchValue))
                ?.or(super.likeIgnoreCase(user.position, userSearchCondition.searchValue))
                ?.or(super.likeIgnoreCase(organization.organizationName, userSearchCondition.searchValue))
                ?.or(super.likeIgnoreCase(user.officeNumber, userSearchCondition.searchValue))
                ?.or(super.likeIgnoreCase(user.mobileNumber, userSearchCondition.searchValue))
                ?.or(user.userKey.eq(userSearchCondition.searchValue))
        )
            .and(
                user.userName.notIn(UserConstants.CREATE_USER_ID)
            )
        if (userSearchCondition.optionalTargets.isNotEmpty()) {
            builder.and(
                (organization.organizationName.`in`(userSearchCondition.optionalTargets))
                    ?.or(user.userKey.`in`(userSearchCondition.optionalTargets))
            )
        }
        if (userSearchCondition.excludeIds.isNotEmpty()) {
            builder.and(user.userKey.notIn(userSearchCondition.excludeIds))
        }
        if (userSearchCondition.isFilterUseYn) {
            builder.and(user.useYn.eq(true))
        }
        return builder
    }
}
