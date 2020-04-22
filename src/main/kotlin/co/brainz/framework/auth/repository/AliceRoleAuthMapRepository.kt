package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapPk
import org.springframework.data.jpa.repository.JpaRepository

interface AliceRoleAuthMapRepository: JpaRepository<AliceRoleAuthMapEntity, AliceRoleAuthMapPk> {
    fun findByAuth(authInfo: AliceAuthEntity): MutableList<AliceRoleAuthMapEntity>
}
