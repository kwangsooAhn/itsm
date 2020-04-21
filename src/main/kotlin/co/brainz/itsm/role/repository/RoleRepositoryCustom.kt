package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity

interface RoleRepositoryCustom {
    /**
     * 전체 역할 목록을 조회한다.
     */
    fun findAll(): MutableList<AliceRoleEntity>
}