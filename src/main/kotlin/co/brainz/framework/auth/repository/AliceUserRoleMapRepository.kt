package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
interface AliceUserRoleMapRepository : JpaRepository<AliceUserRoleMapEntity, String> {
    fun findByRole(roleInfo : AliceRoleEntity): MutableList<AliceUserRoleMapEntity>
}
