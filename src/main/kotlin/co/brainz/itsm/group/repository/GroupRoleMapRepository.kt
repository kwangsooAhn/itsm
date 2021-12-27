package co.brainz.itsm.group.repository

import co.brainz.itsm.group.entity.GroupRoleMapEntity
import co.brainz.itsm.group.entity.GroupRoleMapPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRoleMapRepository : JpaRepository<GroupRoleMapEntity, GroupRoleMapPk>