package co.brainz.itsm.settings.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
    /**
     * 사용자 전체 목록을 조회한다.
     */
    override fun findAll(): MutableList<UserEntity>

    /**
     * 사용자 ID로 해당 사용자 1건을 조회한다.
     */
    fun findByUserId(userId: String): UserEntity

    /**
      * 사용자 아이디를 아이디 순서로 정렬 하도록(역할메뉴에서 사용)
    */
    public fun findByOrderByUserIdAsc(): MutableList<UserEntity>
}