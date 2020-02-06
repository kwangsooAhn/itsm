package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceMenuEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AliceMenuRepository : JpaRepository<AliceMenuEntity, String> {
    fun findByMenuIdIn(menuIds: List<String>): List<AliceMenuEntity>
}
