package co.brainz.workflow.comment.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.workflow.comment.entity.WfCommentEntity
import co.brainz.workflow.comment.mapper.WfCommentMapper
import co.brainz.workflow.comment.repository.WfCommentRepository
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import java.time.LocalDateTime
import java.time.ZoneId
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class WfCommentService(
	private val wfCommentRepository: WfCommentRepository,
	private val wfInstanceRepository: WfInstanceRepository,
	private val aliceUserRepository: AliceUserRepository
) {

    private val wfCommentMapper: WfCommentMapper = Mappers.getMapper(WfCommentMapper::class.java)

    /**
     * Get Instance Comments.
     */
    fun getInstanceComments(instanceId: String): MutableList<RestTemplateCommentDto> {
        val commentList: MutableList<RestTemplateCommentDto> = mutableListOf()
        val commentEntities = wfCommentRepository.findByInstanceId(instanceId)
        commentEntities.forEach { comment ->
            commentList.add(wfCommentMapper.toCommentDto(comment))
        }

        return commentList
    }

    /**
     * Insert Comment.
     */
    fun insertComment(restTemplateCommentDto: RestTemplateCommentDto): Boolean {
        val wfCommentEntity = WfCommentEntity(
            commentId = "",
            content = restTemplateCommentDto.content,
            createDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        wfCommentEntity.aliceUserEntity =
            aliceUserRepository.findAliceUserEntityByUserKey(restTemplateCommentDto.createUserKey.toString())
        wfCommentEntity.instance = restTemplateCommentDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) }
        wfCommentRepository.save(wfCommentEntity)
        return true
    }

    /**
     * Delete Comment.
     */
    fun deleteComment(commentId: String): Boolean {
        wfCommentRepository.deleteById(commentId)
        return true
    }
}
