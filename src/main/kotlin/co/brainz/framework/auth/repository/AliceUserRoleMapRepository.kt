package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.framework.auth.entity.AliceRoleEntity

@Repository
interface AliceUserRoleMapRepository : JpaRepository<AliceUserRoleMapEntity, String> {
    fun findByRole(roleInfo : AliceRoleEntity): MutableList<AliceUserRoleMapEntity>
}
