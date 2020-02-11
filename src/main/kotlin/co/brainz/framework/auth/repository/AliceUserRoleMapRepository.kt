package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapPk
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
public interface AliceUserRoleMapRepository : JpaRepository<AliceUserRoleMapEntity, AliceUserRoleMapPk> {
    public fun findByRole(role: AliceRoleEntity): MutableList<AliceUserRoleMapEntity>
}
