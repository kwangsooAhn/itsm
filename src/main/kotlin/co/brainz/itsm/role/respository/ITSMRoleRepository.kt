package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.role.entity.RoleEntity

@Repository
public interface ITSMRoleRepository : JpaRepository<RoleEntity, String> {
	public override fun findAll(): MutableList<RoleEntity>
	public fun findAllByOrderByRoleNameAsc(): MutableList<RoleEntity>
}