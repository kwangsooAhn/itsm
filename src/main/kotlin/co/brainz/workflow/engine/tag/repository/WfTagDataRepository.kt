package co.brainz.workflow.engine.tag.repository

import co.brainz.workflow.engine.tag.entity.WfTagDataEntity
import co.brainz.workflow.engine.tag.entity.WfTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfTagDataRepository : JpaRepository<WfTagDataEntity, String> {

	fun findByInstanceId(instanceId: String): MutableList<WfTagEntity>
}
