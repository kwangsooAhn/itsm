package co.brainz.workflow.engine.form.mapper

import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfFormMapper {
    fun toFormDto(wfFormEntity: WfFormEntity): RestTemplateFormDto

    @Mappings(
        Mapping(source = "formId", target = "id"),
        Mapping(source = "formName", target = "name"),
        Mapping(source = "formDesc", target = "desc"),
        Mapping(source = "formStatus", target = "status")
    )
    fun toFormViewDto(wfFormEntity: WfFormEntity): RestTemplateFormDto
}
