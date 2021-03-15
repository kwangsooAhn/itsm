package co.brainz.itsm.comment.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.comment.service.WfCommentService
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val wfCommentService: WfCommentService
) {

    /**
     * Set Comment.
     */
    fun setComment(restTemplateCommentDto: RestTemplateCommentDto): Boolean {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateCommentDto.createUserKey = aliceUserDto.userKey
        return wfCommentService.insertComment(restTemplateCommentDto)
    }

    /**
     * Delete Comment.
     */
    fun deleteComment(commentId: String): Boolean {
        return wfCommentService.deleteComment(commentId)
    }
}
