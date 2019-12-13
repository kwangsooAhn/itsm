package co.brainz.framework.menu

import org.springframework.data.jpa.repository.JpaRepository

interface AliceMenuRepository : JpaRepository<AliceMenuEntity, String> {
    fun findByMenuIdIn(menuIds: List<String>): List<AliceMenuEntity>
}