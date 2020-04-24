package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


@Repository
interface RoleRepository : JpaRepository<AliceRoleEntity, String> {
    /**
     * 역할별 상세 내용 조회
     */
    @Query("select a from AliceRoleEntity a inner join fetch a.roleAuthMapEntities where a.roleId = :roleId")
    fun findByRoleId(roleId: String): AliceRoleEntity

    /**
     * 상단 역할명 조회
     */
    @Query("select a from AliceRoleEntity a join fetch a.createUser left outer join a.updateUser")
    fun findByOrderByRoleNameAsc(): MutableList<AliceRoleEntity>

    /*
     * 로그인 시 사용자 역할리스트 조회
    */
    fun findByRoleIdIn(roleId: List<String>): Set<AliceRoleEntity>
}
