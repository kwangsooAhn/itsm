package co.brainz.workflow.token.mapper

import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfTokenMapper {
    @Mappings(
        Mapping(target = "documentId", ignore = true),
        Mapping(target = "documentName", ignore = true),
        Mapping(target = "instanceId", ignore = true),
        Mapping(target = "instanceCreateUser", ignore = true),
        Mapping(target = "complete", ignore = true),
        Mapping(target = "elementId", ignore = true),
        Mapping(target = "elementType", ignore = true),
        Mapping(target = "data", ignore = true),
        Mapping(target = "fileDataIds", ignore = true),
        Mapping(target = "parentTokenId", ignore = true),
        Mapping(target = "processId", ignore = true),
        Mapping(target = "actions", ignore = true)
    )
    fun toTokenDto(wfTokenEntity: WfTokenEntity): RestTemplateTokenDto

    @Mappings(
        Mapping(target = "componentId", ignore = true)
    )
    fun toTokenDataDto(wfTokenDataEntity: WfTokenDataEntity): RestTemplateTokenDataDto
}
