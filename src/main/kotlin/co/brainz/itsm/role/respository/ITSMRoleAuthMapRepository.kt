package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.role.entity.RoleAuthMapEntity
import org.springframework.data.repository.CrudRepository
import javax.persistence.EntityManager

@Repository
public interface ITSMRoleAuthMapRepository : JpaRepository<RoleAuthMapEntity, String> {

	//역할별 권한 조회하기  findByRoleId
	public fun findByRoleIdOrderByAuthIdAsc(roleId: String): MutableList<RoleAuthMapEntity>
}