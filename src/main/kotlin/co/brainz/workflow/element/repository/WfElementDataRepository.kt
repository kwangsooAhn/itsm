package co.brainz.workflow.element.repository

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementDataPk
import co.brainz.workflow.element.entity.WfElementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfElementDataRepository : JpaRepository<WfElementDataEntity, WfElementDataPk> {

    fun findByElementAndAttributeId(
        element: WfElementEntity,
        endId: String = WfElementConstants.AttributeId.TARGET_ID.value
    ): WfElementDataEntity
}
