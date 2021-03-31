/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<AliceRoleEntity, String>,
    JpaSpecificationExecutor<AliceRoleEntity>, RoleRepositoryCustom {

    /**
     * 역할 전체 목록을 조회한다.
     */
    override fun findAll(): MutableList<AliceRoleEntity>

    /**
     * 역할별 상세 내용 조회
     */
    fun findByRoleId(roleId: String): AliceRoleEntity

    /*
     * 로그인 시 사용자 역할리스트 조회
    */
    fun findByRoleIdIn(roleId: List<String>): Set<AliceRoleEntity>
}
