package co.brainz.itsm.group.repository

import co.brainz.itsm.group.entity.GroupEntity
import co.brainz.itsm.group.entity.GroupRoleMapEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRoleMapRepository : JpaRepository<GroupRoleMapEntity, String>, GroupRoleMapRepositoryCustom {

    fun deleteByGroupId(groupId: GroupEntity)
}
