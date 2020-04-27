package co.brainz.workflow.engine.comment.mapper

import co.brainz.workflow.engine.comment.dto.WfCommentDto
import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import org.mapstruct.Mapper

@Mapper
interface WfCommentMapper {
    fun toCommentDto(wfCommentEntity: WfCommentEntity): WfCommentDto
}
