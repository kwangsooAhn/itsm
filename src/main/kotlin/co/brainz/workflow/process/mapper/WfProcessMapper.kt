package co.brainz.workflow.process.mapper

import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.process.entity.WfProcessEntity
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
        Mapping(source = "createUser.userKey", target = "createUserKey"),
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userKey", target = "updateUserKey"),
        Mapping(source = "updateUser.userName", target = "updateUserName"),
        Mapping(target = "enabled", ignore = true),
        Mapping(target = "totalCount", ignore = true)
    )
    fun toProcessViewDto(processEntity: WfProcessEntity): RestTemplateProcessViewDto

    @Mappings(
        Mapping(target = "elementEntities", ignore = true),
        Mapping(target = "document", ignore = true),
        Mapping(target = "createUser", ignore = true),
        Mapping(target = "updateUser", ignore = true)
    )
    fun toProcessEntity(restTemplateProcessDto: RestTemplateProcessDto): WfProcessEntity

    @Mappings(
        Mapping(source = "elementId", target = "id"),
        Mapping(source = "elementType", target = "type"),
        Mapping(source = "elementName", target = "name"),
        Mapping(source = "elementDesc", target = "description"),
        Mapping(target = "display", ignore = true),
        Mapping(target = "data", ignore = true),
        Mapping(target = "required", ignore = true)
    )
    fun toWfElementDto(elementEntity: WfElementEntity): RestTemplateElementDto
}
