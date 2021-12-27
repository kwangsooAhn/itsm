package co.brainz.itsm.group.repository

import co.brainz.itsm.group.entity.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : JpaRepository<GroupEntity, String>
