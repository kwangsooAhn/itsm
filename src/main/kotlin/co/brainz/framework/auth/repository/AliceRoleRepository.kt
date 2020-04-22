package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AliceRoleRepository: JpaRepository<AliceRoleEntity, String> {
    fun findByRoleIdIn(roleId: List<Int>): List<AliceRoleEntity>
}
