package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceUrlEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AliceAuthRepository: JpaRepository<AliceAuthEntity, String> {
    /**
     * 권한 리스트 조회
     */
    @Query("select a from AliceAuthEntity a join fetch a.createUser left outer join a.updateUser")
    fun findByOrderByAuthIdAsc(): MutableList<AliceAuthEntity>

    /**
     * 역할별 권한 조회
     */
    fun findByAuthIdIn(roleId: MutableSet<String>): MutableSet<AliceAuthEntity>

    @Query("SELECT u " +
            "FROM AliceUrlEntity u, AliceUrlAuthMapEntity ua, AliceRoleAuthMapEntity ra, AliceUserRoleMapEntity ur " +
            "WHERE ur.role = ra.role " +
            "AND ra.auth = ua.auth " +
            "AND ua.url = u " +
            "AND ur.user.userKey = :userKey")
    fun findByUserKey(userKey: String): MutableSet<AliceUrlEntity>
}
