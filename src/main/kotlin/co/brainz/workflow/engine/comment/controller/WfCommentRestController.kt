package co.brainz.workflow.engine.comment.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.comment.dto.WfCommentDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/comments")
class WfCommentRestController(
    private val wfEngine: WfEngine
) {

    @PostMapping("/{tokenId}")
    fun postComment(@PathVariable tokenId: String, @RequestBody wfCommentDto: WfCommentDto): Boolean {
        return wfEngine.comment().postComment(wfCommentDto)
    }

    @GetMapping("/{tokenId}")
    fun getComments(@PathVariable tokenId: String): MutableList<WfCommentDto> {
        return wfEngine.comment().getComments(tokenId)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: String): Boolean {
        return wfEngine.comment().deleteComment(commentId)
    }
}
