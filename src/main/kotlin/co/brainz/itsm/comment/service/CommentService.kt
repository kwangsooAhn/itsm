package co.brainz.itsm.comment.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.comment.service.WfCommentService
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val wfCommentService: WfCommentService,
    private val currentSessionUser: CurrentSessionUser
) {

    /**
     * Set Comment.
     */
    fun setComment(restTemplateCommentDto: RestTemplateCommentDto): Boolean {
        restTemplateCommentDto.createUserKey = currentSessionUser.getUserKey()
        return wfCommentService.insertComment(restTemplateCommentDto)
    }

    /**
     * Delete Comment.
     */
    fun deleteComment(commentId: String): Boolean {
        return wfCommentService.deleteComment(commentId)
    }
}
