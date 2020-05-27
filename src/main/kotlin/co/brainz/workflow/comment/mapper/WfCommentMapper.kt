package co.brainz.workflow.comment.mapper

import co.brainz.workflow.comment.entity.WfCommentEntity
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfCommentMapper {
    @Mappings(
        Mapping(source = "instance.instanceId", target = "instanceId")
    )
    fun toCommentDto(wfCommentEntity: WfCommentEntity): RestTemplateCommentDto
}
