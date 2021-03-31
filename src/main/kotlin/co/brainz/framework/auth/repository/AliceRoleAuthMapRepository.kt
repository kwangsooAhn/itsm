/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapPk
import org.springframework.data.jpa.repository.JpaRepository

interface AliceRoleAuthMapRepository : JpaRepository<AliceRoleAuthMapEntity, AliceRoleAuthMapPk>,
    AliceRoleAuthMapRepositoryCustom {
    fun countByAuth(authInfo: AliceAuthEntity): Int
}
