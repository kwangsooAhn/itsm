package co.brainz.workflow.engine.form.mapper

import co.brainz.workflow.engine.form.dto.WfFormDto
import co.brainz.workflow.engine.form.entity.WfFormEntity
import org.mapstruct.Mapper

@Mapper
interface WfFormMapper {
    fun toFormDto(wfFormEntity: WfFormEntity): WfFormDto
}
