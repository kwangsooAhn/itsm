package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.role.entity.ITSMRoleEntity

@Repository
public interface ITSMRoleRepository : JpaRepository<ITSMRoleEntity, String> {
	//각 역할별 상세 내용 조회
	public fun findByRoleId(roleId: String): MutableList<ITSMRoleEntity>

	//상단 역할명 조회
	public fun findByOrderByRoleNameAsc(): MutableList<ITSMRoleEntity>

}