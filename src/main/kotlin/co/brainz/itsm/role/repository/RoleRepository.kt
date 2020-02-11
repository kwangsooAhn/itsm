package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository


@Repository
interface RoleRepository : JpaRepository<AliceRoleEntity, String> {
    /**
     * 역할별 상세 내용 조회
     */
    fun findByRoleId(roleId: String): AliceRoleEntity

    /**
     * 상단 역할명 조회
     */
    fun findByOrderByRoleNameAsc(): MutableList<AliceRoleEntity>

    /*
     * 로그인 시 사용자 역할리스트 조회
    */
    fun findByRoleIdIn(roleId: List<String>): Set<AliceRoleEntity>

}
