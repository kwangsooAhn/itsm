/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceAuthRepository : JpaRepository<AliceAuthEntity, String> {

    /**
     * 권한 리스트 조회
     */
    fun findAllByOrderByAuthName(): List<AliceAuthEntity>

    /**
     * 역할별 권한 조회
     */
    fun findByAuthIdIn(roleId: MutableSet<String>): MutableSet<AliceAuthEntity>
}
