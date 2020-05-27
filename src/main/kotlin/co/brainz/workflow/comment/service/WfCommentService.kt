package co.brainz.workflow.comment.service

import co.brainz.workflow.comment.entity.WfCommentEntity
import co.brainz.workflow.comment.repository.WfCommentRepository
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import java.time.LocalDateTime
import java.time.ZoneId
import org.springframework.stereotype.Service

@Service
class WfCommentService(
    private val wfCommentRepository: WfCommentRepository,
    private val wfInstanceRepository: WfInstanceRepository
) {

    /**
     * Insert Comment.
     */
    fun insertComment(restTemplateCommentDto: RestTemplateCommentDto): Boolean {
        val wfCommentEntity = WfCommentEntity(
            commentId = "",
            content = restTemplateCommentDto.content,
            createDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        wfCommentEntity.createUserKey = restTemplateCommentDto.createUserKey.toString()
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
