package co.brainz.workflow.engine.component.mapper

import co.brainz.workflow.engine.component.entity.WfComponentDataEntity
import co.brainz.workflow.engine.form.dto.WfFormComponentDataDto
import org.mapstruct.Mapper

@Mapper
interface WfComponentDataMapper {
    fun toComponentDataDto(wfComponentDataEntity: WfComponentDataEntity): WfFormComponentDataDto
}
