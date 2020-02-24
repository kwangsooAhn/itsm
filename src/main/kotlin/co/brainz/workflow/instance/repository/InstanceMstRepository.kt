package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.entity.InstanceMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface InstanceMstRepository: JpaRepository<InstanceMstEntity, String> {

    fun findInstanceMstEntityByInstId(instId: String): Optional<InstanceMstEntity>
}
