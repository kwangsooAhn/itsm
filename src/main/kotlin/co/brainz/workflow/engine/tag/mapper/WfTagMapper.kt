package co.brainz.workflow.engine.tag.mapper

import co.brainz.workflow.engine.tag.entity.WfTagDataEntity
import co.brainz.workflow.provider.dto.RestTemplateTagDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfTagMapper {
    @Mappings(
        Mapping(source = "instance.instanceId", target = "instanceId")
    )
    fun toTagDto(wfTagDataEntity: WfTagDataEntity): RestTemplateTagDto
}
