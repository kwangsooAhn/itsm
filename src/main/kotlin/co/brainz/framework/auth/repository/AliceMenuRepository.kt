package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceMenuEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AliceMenuRepository : JpaRepository<AliceMenuEntity, String> {
    fun findByOrderByMenuIdAsc(): MutableList<AliceMenuEntity>

    fun findByMenuIdIn(menuIds: List<String>): List<AliceMenuEntity>

    @Query(
        "SELECT m " +
                "FROM AliceMenuEntity m, AliceMenuAuthMapEntity ma, AliceRoleAuthMapEntity ra, AliceUserRoleMapEntity ur " +
                "WHERE ur.role = ra.role " +
                "AND ra.auth = ma.auth " +
                "AND ma.menu = m " +
                "AND ur.user.userKey = :userKey"
    )
    fun findByUserKey(@Param("userKey") userKey: String): MutableSet<AliceMenuEntity>
}
