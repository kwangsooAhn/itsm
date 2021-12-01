/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<AliceUserEntity, String>, JpaSpecificationExecutor<AliceUserEntity>,
    UserRepositoryCustom {

    /**
     * 사용자 ID로 해당 사용자 1건을 조회한다.
     */
    fun findByUserId(userId: String): AliceUserEntity

    /**
     * 사용자 oauthKey, 플랫폼으로 해당 사용자 정보를 조회한다.
     */
    fun findByOauthKeyAndPlatform(oauthKey: String, platform: String): Optional<AliceUserEntity>

    /**
     * 사용자의 ID의 개수를 리턴한다.
     */
    fun countByUserId(userId: String): Int

    /**
     *  사용자 목록을 사용자명으로 정렬조회한다.
     */
    fun findByOrderByUserNameAsc(): MutableList<AliceUserEntity>

    /**
     * 특정 사용자를 제외하고 사용자 테이블의 전체 개수 리턴
     */
    fun countByUserIdNotContaining(userId: String): Long
}
