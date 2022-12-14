/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.entity.UserCustomEntity
import co.brainz.itsm.user.entity.UserCustomEntityPk
import javax.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

interface UserCustomRepository : JpaRepository<UserCustomEntity, UserCustomEntityPk>, UserCustomRepositoryCustom {
    @Transactional
    fun deleteByUserAndAndCustomType(user: AliceUserEntity, customType: String): Int
}
