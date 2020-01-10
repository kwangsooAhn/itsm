package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository


@Repository
public interface RoleRepository : JpaRepository<AliceRoleEntity, String> {
    /**
     * 역할별 상세 내용 조회
     */
    public fun findByRoleId(roleId: String): MutableList<AliceRoleEntity>

    /**
     * 상단 역할명 조회
     */
    public fun findByOrderByRoleNameAsc(): MutableList<AliceRoleEntity>

    public fun findByRoleIdIn(roleId: List<String>): Set<AliceRoleEntity>

}
