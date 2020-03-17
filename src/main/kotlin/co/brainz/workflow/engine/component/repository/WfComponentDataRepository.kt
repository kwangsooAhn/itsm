package co.brainz.workflow.engine.component.repository

import co.brainz.workflow.engine.component.entity.WfComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfComponentDataRepository: JpaRepository<WfComponentDataEntity, String> {

}
