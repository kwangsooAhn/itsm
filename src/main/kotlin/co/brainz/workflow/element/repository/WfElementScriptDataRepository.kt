package co.brainz.workflow.element.repository

import co.brainz.workflow.element.entity.WfElementScriptDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfElementScriptDataRepository : JpaRepository<WfElementScriptDataEntity, String>
