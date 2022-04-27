/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.organization.entity.QOrganizationEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.dto.UserListExcelDto
import co.brainz.itsm.user.dto.UserSearchCondition
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl : QuerydslRepositorySupport(AliceUserEntity::class.java), UserRepositoryCustom {
    val user = QAliceUserEntity.aliceUserEntity
    val organization = QOrganizationEntity.organizationEntity
    val pageable = Pageable.unpaged()

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
            .where(
                super.likeIgnoreCase(user.userName, userSearchCondition.searchValue)
                    ?.or(super.likeIgnoreCase(user.userId, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.position, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(organization.organizationName, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.officeNumber, userSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(user.mobileNumber, userSearchCondition.searchValue))
            )
            .where(
                user.userName.notIn(AliceUserConstants.CREATE_USER_ID)
            )
        if (userSearchCondition.optionalTargets.isNotEmpty()) {
            query.where(
                (organization.organizationName.`in`(userSearchCondition.optionalTargets))
                ?.or(user.userKey.`in`(userSearchCondition.optionalTargets))
            )
        }
        if (userSearchCondition.excludeIds.isNotEmpty()) {
            query.where(user.userKey.notIn(userSearchCondition.excludeIds))
        }
        if (userSearchCondition.isFilterUseYn) {
            query.where(user.useYn.eq(true))
        }
        query.orderBy(user.userName.asc())
val totalCount = query.fetch().size
        if (userSearchCondition.isPaging) {
            query.limit(userSearchCondition.contentNumPerPage)
            query.offset((userSearchCondition.pageNum - 1) * userSearchCondition.contentNumPerPage)
        }
        val countQuery = from(user)
            .select(user.count())
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
                user.userName.notIn(AliceUserConstants.CREATE_USER_ID)
            )
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
                user.userName.notIn(AliceUserConstants.CREATE_USER_ID)
            )
            .orderBy(user.userName.asc())

        return query.fetch()
    }

    override fun getUserListInOrganization(organizationIds: Set<String>): List<AliceUserEntity> {
        return from(user)
            .where(user.department.`in`(organizationIds))
            .fetch()
    }
}
