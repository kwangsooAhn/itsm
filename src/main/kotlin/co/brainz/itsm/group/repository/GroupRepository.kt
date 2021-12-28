package co.brainz.itsm.group.repository

import co.brainz.itsm.group.dto.GroupDto
import co.brainz.itsm.group.entity.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository: JpaRepository<GroupEntity,String> ,GroupRepositoryCustom {

}