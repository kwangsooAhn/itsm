package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.user.dto.UserCustomDto

interface UserCustomRepositoryCustom : AliceRepositoryCustom {
    fun findByUserAndCustomType(user: AliceUserEntity, customType: String): UserCustomDto?
}