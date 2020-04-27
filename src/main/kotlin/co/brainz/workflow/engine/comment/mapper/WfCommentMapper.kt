package co.brainz.workflow.engine.comment.mapper

import co.brainz.workflow.engine.comment.dto.WfCommentDto
import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfCommentMapper {
    @Mappings(
        Mapping(source = "instance.instanceId", target = "instanceId")
    )
    fun toCommentDto(wfCommentEntity: WfCommentEntity): WfCommentDto
}
