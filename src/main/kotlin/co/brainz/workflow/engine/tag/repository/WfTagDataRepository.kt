package co.brainz.workflow.engine.tag.repository

import co.brainz.workflow.engine.tag.entity.WfTagDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfTagDataRepository : JpaRepository<WfTagDataEntity, String> {

}
