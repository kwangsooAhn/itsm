package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.EntityManager
import co.brainz.itsm.role.entity.RoleAuthMapEntity

@Repository
public interface RoleAuthMapRepository : JpaRepository<RoleAuthMapEntity, String> {

	//역할별사용자아이디
	public fun findByRoleIdOrderByRoleIdAsc(roleId: String): MutableList<RoleAuthMapEntity>
}