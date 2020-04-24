package co.brainz.itsm.comment.controller

import co.brainz.itsm.comment.service.CommentService
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/comments")
class CommentRestController(
    private val commentService: CommentService
) {

    @PostMapping("/{tokenId}")
    fun setComment(@PathVariable tokenId: String, @RequestBody restTemplateCommentDto: RestTemplateCommentDto): Boolean {
        return commentService.setComment(restTemplateCommentDto)
    }
}
