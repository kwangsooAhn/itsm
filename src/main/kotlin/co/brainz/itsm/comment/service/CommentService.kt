package co.brainz.itsm.comment.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val restTemplate: RestTemplateProvider
) {

    /**
     * Set Comment.
     */
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

    /**
     * Get Comments.
     */
    fun getComments(tokenId: String): List<RestTemplateCommentDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Comment.GET_COMMENT.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )
        val responseBody = restTemplate.get(url)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        val restTemplateComments: List<RestTemplateCommentDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateCommentDto::class.java))
        for (comment in restTemplateComments) {
            comment.createDt = comment.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }

        return restTemplateComments
    }

    /**
     * Delete Comment.
     */
    fun deleteComment(commentId: String): ResponseEntity<String> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Comment.DELETE_COMMENT.url.replace(
                restTemplate.getKeyRegex(),
                commentId
            )
        )

        return restTemplate.delete(url)
    }
}
