package co.brainz.workflow.comment.controller

import co.brainz.workflow.comment.service.WfCommentService
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/comments")
class WfCommentRestController(
    private val wfCommentService: WfCommentService
) {

    @PostMapping("")
    fun insertComment(@RequestBody restTemplateCommentDto: RestTemplateCommentDto): Boolean {
        return wfCommentService.insertComment(restTemplateCommentDto)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: String): Boolean {
        return wfCommentService.deleteComment(commentId)
    }
}
