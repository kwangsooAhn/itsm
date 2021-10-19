package co.brainz.itsm.comment.mapper

import co.brainz.itsm.comment.dto.InstanceCommentDto
import co.brainz.itsm.comment.entity.WfCommentEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface CommentMapper {
    @Mappings(
        Mapping(source = "instance.instanceId", target = "instanceId"),
        Mapping(source = "aliceUserEntity.userKey", target = "createUserKey"),
        Mapping(source = "aliceUserEntity.userName", target = "createUserName"),
        Mapping(target = "tokenId", ignore = true)
    )
    fun toCommentDto(wfCommentEntity: WfCommentEntity): InstanceCommentDto
}
