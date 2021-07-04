/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository : JpaRepository<AliceAuthEntity, String>, AuthRepositoryCustom {
    /**
     * 권한 상세 정보 조회
     */
    fun findByAuthId(authId: String): AliceAuthEntity
}
