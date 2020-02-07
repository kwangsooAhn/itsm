package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
public interface AliceUserRoleMapRepository : JpaRepository<AliceUserRoleMapEntity, String> {
    public fun findByRoleId(roleId: String): MutableList<AliceUserRoleMapEntity>
}
