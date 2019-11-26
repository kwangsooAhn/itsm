package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.role.entity.RoleAuthMapEntity


@Repository
public interface ITSMRoleAuthMapRepository : JpaRepository<RoleAuthMapEntity, String> {
	public override fun findAll(): MutableList<RoleAuthMapEntity>
}