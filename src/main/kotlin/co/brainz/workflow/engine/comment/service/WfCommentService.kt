package co.brainz.workflow.engine.comment.service

import co.brainz.workflow.engine.comment.dto.WfCommentDto
import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import co.brainz.workflow.engine.comment.repository.WfCommentRepository
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WfCommentService(
    private val wfCommentRepository: WfCommentRepository,
    private val wfInstanceRepository: WfInstanceRepository
) {

    /**
     * Save Comment.
     */
    fun postComment(wfCommentDto: WfCommentDto): Boolean {
        val wfCommentEntity = WfCommentEntity(
            commentId = "",
            content = wfCommentDto.content,
            createDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        wfCommentEntity.createUserKey = wfCommentDto.createUserKey.toString()
        wfCommentEntity.instance = wfCommentDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) }
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
