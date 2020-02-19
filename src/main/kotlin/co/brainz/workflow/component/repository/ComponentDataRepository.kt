package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.ComponentDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ComponentDataRepository: JpaRepository<ComponentDataEntity, String> {

}
