package co.brainz.workflow.engine.instance.mapper

import co.brainz.workflow.engine.instance.dto.WfInstanceViewDto
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import org.mapstruct.Mapper

@Mapper
interface WfInstanceMapper {
    fun toInstanceViewDto(wfInstanceEntity: WfInstanceEntity): WfInstanceViewDto
}
