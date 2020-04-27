package co.brainz.workflow.engine.comment.service

import co.brainz.workflow.engine.comment.dto.WfCommentDto
import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import co.brainz.workflow.engine.comment.mapper.WfCommentMapper
import co.brainz.workflow.engine.comment.repository.WfCommentRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WfCommentService(
    private val wfCommentRepository: WfCommentRepository,
    private val wfTokenRepository: WfTokenRepository
) {

    private val wfCommentMapper: WfCommentMapper = Mappers.getMapper(WfCommentMapper::class.java)

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

        wfCommentEntity.instance = wfCommentDto.tokenId?.let { wfTokenRepository.findTokenEntityByTokenId(it).get().instance }
        wfCommentRepository.save(wfCommentEntity)
        return true
    }

    /**
     * Get Comments.
     */
    fun getComments(tokenId: String): MutableList<WfCommentDto> {
        val commentList: MutableList<WfCommentDto> = mutableListOf()
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        if (tokenEntity.isPresent) {
            tokenEntity.get().instance.comments?.forEach { comment ->
                commentList.add(wfCommentMapper.toCommentDto(comment))
            }
        }

        return commentList
    }

    /**
     * Delete Comment.
     */
    fun deleteComment(commentId: String): Boolean {
        wfCommentRepository.deleteById(commentId)
        return true
    }
}
