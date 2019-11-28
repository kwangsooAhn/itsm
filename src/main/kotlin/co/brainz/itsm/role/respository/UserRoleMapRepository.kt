package co.brainz.itsm.role.respository

import javax.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.role.entity.UserRoleMapEntity

@Repository
public interface UserRoleRepository : JpaRepository<UserRoleMapEntity, String> {

	//역할별사용자아이디
	public fun findByRoleIdOrderByRoleIdAsc(roleId: String): MutableList<UserRoleMapEntity>
}