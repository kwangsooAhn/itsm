package co.brainz.workflow.engine.process.mapper

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import co.brainz.workflow.provider.dto.RestTemplateElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfProcessMapper {
    @Mappings(
        Mapping(source = "processId", target = "id"),
        Mapping(source = "processName", target = "name"),
        Mapping(source = "processDesc", target = "description"),
        Mapping(source = "processStatus", target = "status"),
        Mapping(source = "createDt", target = "createDt"),
        Mapping(source = "createUserKey", target = "createUserKey"),
        Mapping(source = "updateDt", target = "updateDt"),
        Mapping(source = "updateUserKey", target = "updateUserKey")
    )
    fun toProcessViewDto(processEntity: WfProcessEntity): RestTemplateProcessViewDto

    fun toProcessEntity(restTemplateProcessDto: RestTemplateProcessDto): WfProcessEntity

    @Mappings(
        Mapping(source = "elementId", target = "id"),
        Mapping(source = "elementType", target = "type")
    )
    fun toWfElementDto(elementEntity: WfElementEntity): RestTemplateElementDto
}
