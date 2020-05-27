package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceUserRoleMapRepository : JpaRepository<AliceUserRoleMapEntity, AliceUserRoleMapPk>, AliceUserRoleMapRepositoryCustom {
    fun findByRole(roleInfo: AliceRoleEntity): MutableList<AliceUserRoleMapEntity>
}
