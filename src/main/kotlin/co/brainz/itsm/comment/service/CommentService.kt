package co.brainz.itsm.comment.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val restTemplate: RestTemplateProvider
) {

    fun setComment(restTemplateCommentDto: RestTemplateCommentDto): Boolean {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateCommentDto.createUserKey = aliceUserDto.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Comment.POST_COMMENT.url.replace(
                restTemplate.getKeyRegex(),
                restTemplateCommentDto.tokenId!!
            )
        )
        val responseEntity = restTemplate.create(url, restTemplateCommentDto)
        return responseEntity.body.toString().isNotEmpty()
    }
}
