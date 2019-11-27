package co.brainz.itsm.settings.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserEntity, String> {
    /**
     * 사용자 전체 목록을 조회한다.
     */
    override fun findAll(): MutableList<UserEntity>

    /**
     * 사용자 ID로 해당 사용자 1건을 조회한다.
     */
    fun findByUserId(userId: String): UserEntity

    /**
     * 사용자 ID로 해당 사용자 1건을 삭제한다.
     */
    fun deleteByUserId(userId: String): Boolean

    fun save(entity: UserEntity): UserEntity

    override fun getOne(id: String): UserEntity
}