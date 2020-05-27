package co.brainz.workflow.engine.comment.mapper

import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfCommentMapper {
    @Mappings(
        Mapping(source = "instance.instanceId", target = "instanceId"),
        Mapping(source = "aliceUserEntity.userKey", target = "createUserKey"),
        Mapping(source = "aliceUserEntity.userName", target = "createUserName")
    )
    fun toCommentDto(wfCommentEntity: WfCommentEntity): RestTemplateCommentDto
}
