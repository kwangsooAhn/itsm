package co.brainz.framework.menu.repository

import co.brainz.framework.menu.entity.AliceMenuEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AliceMenuRepository : JpaRepository<AliceMenuEntity, String> {
    fun findByMenuIdIn(menuIds: List<String>): List<AliceMenuEntity>
}
