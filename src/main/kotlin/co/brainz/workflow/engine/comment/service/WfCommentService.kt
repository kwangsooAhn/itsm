package co.brainz.workflow.engine.comment.service

import co.brainz.workflow.engine.comment.dto.WfCommentDto
import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import co.brainz.workflow.engine.comment.repository.WfCommentRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WfCommentService(
    private val wfCommentRepository: WfCommentRepository,
    private val wfTokenRepository: WfTokenRepository
) {

    fun postComment(wfCommentDto: WfCommentDto): Boolean {
        val wfCommentEntity = WfCommentEntity(
            commentId = "",
            commentValue = wfCommentDto.comment,
            createDt = LocalDateTime.now(ZoneId.of("UTC")),
            createUserKey = wfCommentDto.createUserKey
        )

        wfCommentEntity.instance = wfCommentDto.tokenId?.let { wfTokenRepository.findTokenEntityByTokenId(it).get().instance }
        wfCommentRepository.save(wfCommentEntity)
        return true
    }
}
