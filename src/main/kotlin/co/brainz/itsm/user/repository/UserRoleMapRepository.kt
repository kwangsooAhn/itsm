package co.brainz.itsm.user.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.user.entity.UserRoleMapEntity

@Repository
public interface UserRoleMapRepository : JpaRepository<UserRoleMapEntity, String> {

    public fun findByRoleId(roleId: String): MutableList<UserRoleMapEntity>

}
