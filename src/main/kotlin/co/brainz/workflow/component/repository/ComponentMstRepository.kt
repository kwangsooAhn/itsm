package co.brainz.workflow.component.repository

import co.brainz.workflow.process.entity.ProcessMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ComponentMstRepository: JpaRepository<ProcessMstEntity, String> {

}

