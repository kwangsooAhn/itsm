package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository: JpaRepository<AliceUserEntity, String>, JpaSpecificationExecutor<AliceUserEntity> {
    /**
     * 사용자 전체 목록을 조회한다.
     */
    override fun findAll(): MutableList<AliceUserEntity>

    /**
     * 사용자 ID로 해당 사용자 1건을 조회한다.
     */
    fun findByUserId(userId: String): AliceUserEntity

    /**
     * 사용자 ID, 플랫폼로 해당 사용자 정보를 조회한다.
     */
    fun findByUserIdAndPlatform(userId: String, platform: String): Optional<AliceUserEntity>

    /**
     * 사용자 oauthKey, 플랫폼으로 해당 사용자 정보를 조회한다.
     */
    fun findByOauthKeyAndPlatform(oauthKey: String, platform: String): Optional<AliceUserEntity>

    /**
     * 사용자의 KEY로 해당 사용자 1건을 조회한다.
     */
    fun findByUserKey(userKey: String): AliceUserEntity

    /**
     * 사용자의 ID의 개수를 리턴한다.
     */
    fun countByUserId(userId: String): Int

    /**
     *  사용자 목록을 사용자명으로 정렬조회한다.
     */
    fun findByOrderByUserNameAsc(): MutableList<AliceUserEntity>
}
