package co.brainz.itsm.user

import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<RoleEntity, String> {
    fun findByRoleIdIn(roleId: Collection<String>): Set<RoleEntity>
}