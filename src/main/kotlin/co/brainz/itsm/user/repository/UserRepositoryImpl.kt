/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.dto.UserListDto
import co.brainz.itsm.user.dto.UserListReturnDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl : QuerydslRepositorySupport(AliceUserEntity::class.java), UserRepositoryCustom {
    override fun findAliceUserEntityList(
        search: String,
        offset: Long
    ): UserListReturnDto {
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
                    user.uploaded,
                    user.uploadedLocation,
                    user.createDt
                )
            )
            .leftJoin(code).on(code.code.eq(user.department))
            .where(
                super.like(user.userName, search)?.or(super.like(user.userId, search))
                    ?.or(super.like(user.position, search))
                    ?.or(super.like(user.department, search))?.or(super.like(user.officeNumber, search))
                    ?.or(super.like(user.mobileNumber, search))
            )
            .orderBy(user.userName.asc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)
            .fetchResults()

        val userList = mutableListOf<UserListDto>()
        for (data in query.results) {
            val userDto = UserListDto(
                userKey = data.userKey,
                userId = data.userId,
                userName = data.userName,
                email = data.email,
                position = data.position,
                department = data.department,
                officeNumber = data.officeNumber,
                mobileNumber = data.mobileNumber,
                avatarType = data.avatarType,
                avatarValue = data.avatarValue,
                uploaded = data.uploaded,
                uploadedLocation = data.uploadedLocation
            )
            userList.add(userDto)
        }
        return UserListReturnDto(
            data = userList,
            totalCount = query.total
        )
    }
}
