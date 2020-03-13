package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.WfComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfComponentDataRepository: JpaRepository<WfComponentDataEntity, String> {

}
