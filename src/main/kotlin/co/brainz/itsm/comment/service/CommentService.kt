package co.brainz.itsm.comment.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.comment.dto.InstanceCommentDto
import co.brainz.itsm.comment.entity.WfCommentEntity
import co.brainz.itsm.comment.mapper.CommentMapper
import co.brainz.itsm.comment.repository.CommentRepository
import co.brainz.workflow.instance.repository.WfInstanceRepository
import java.time.LocalDateTime
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val currentSessionUser: CurrentSessionUser
) {
    private val commentMapper: CommentMapper = Mappers.getMapper(CommentMapper::class.java)

    /**
     * Get Instance Comments.
     */
    fun getInstanceComments(instanceId: String): List<InstanceCommentDto> {
        val commentList: MutableList<InstanceCommentDto> = mutableListOf()
        val commentEntities = commentRepository.findByInstanceId(instanceId)
        commentEntities.forEach { comment ->
            commentList.add(commentMapper.toCommentDto(comment))
        }
        return commentList.toList()
    }

    /**
     * Set Comment.
     */
    fun setComment(instanceCommentDto: InstanceCommentDto): Boolean {
        instanceCommentDto.createUserKey = currentSessionUser.getUserKey()
        val commentEntity = WfCommentEntity(
            commentId = "",
            content = instanceCommentDto.content,
            createDt = LocalDateTime.now()
        )
        commentEntity.aliceUserEntity =
            aliceUserRepository.findAliceUserEntityByUserKey(instanceCommentDto.createUserKey.toString())
        commentEntity.instance = instanceCommentDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) }
        commentRepository.save(commentEntity)
        return true
    }

    /**
     * Delete Comment.
     */
    fun deleteComment(commentId: String): Boolean {
        commentRepository.deleteById(commentId)
        return true
    }
}
