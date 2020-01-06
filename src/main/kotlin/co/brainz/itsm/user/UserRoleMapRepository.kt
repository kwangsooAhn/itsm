package co.brainz.itsm.user

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.user.UserRoleMapEntity

@Repository
public interface UserRoleMapRepository : JpaRepository<UserRoleMapEntity, String> {

    public fun findByRoleId(roleId: String): MutableList<UserRoleMapEntity>

}