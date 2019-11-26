package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.role.entity.UserRoleMapEntity

import org.springframework.data.repository.CrudRepository
import javax.persistence.EntityManager

@Repository
public interface ITSMUserRoleRepository : JpaRepository<UserRoleMapEntity, String> {

	//역할별사용자아이디
	public fun findByRoleIdOrderByRoleIdAsc(roleId: String): MutableList<UserRoleMapEntity>
}