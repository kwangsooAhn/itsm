/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AliceUserRepository : JpaRepository<AliceUserEntity, String> {
    @Query("SELECT a FROM AliceUserEntity a WHERE a.userId = :userId")
    fun findByUserId(@Param("userId") userId: String): AliceUserEntity

    fun findByOauthKeyAndPlatform(oauthKey: String, platform: String): AliceUserEntity

    fun findAliceUserEntityByUserKey(userKey: String): AliceUserEntity

    /**
     * 사용자의 KEY로 해당 사용자 1건을 조회한다.
     */
    fun findByUserKey(userKey: String): AliceUserEntity

    fun existsByUserId(userId: String): Boolean
}
