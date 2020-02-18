package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface AliceUserRoleMapRepository : JpaRepository<AliceUserRoleMapEntity, String> {
    fun findByRole(roleInfo : AliceRoleEntity): MutableList<AliceUserRoleMapEntity>

    @Query("SELECT r.role FROM AliceUserRoleMapEntity r WHERE r.user.userKey = :userKey")
    fun findByUserKey(@Param("userKey") userKey: String): MutableList<AliceRoleEntity>
}
