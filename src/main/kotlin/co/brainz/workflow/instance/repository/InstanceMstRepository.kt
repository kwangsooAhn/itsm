package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.entity.InstanceMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface InstanceMstRepository: JpaRepository<InstanceMstEntity, String> {

    fun findInstanceMstEntityByInstanceId(instanceId: String): Optional<InstanceMstEntity>

    @Query()
    fun findInstances()
}
