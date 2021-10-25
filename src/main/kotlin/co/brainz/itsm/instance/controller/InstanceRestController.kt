package co.brainz.itsm.instance.controller

import co.brainz.itsm.instance.dto.InstanceCommentDto
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/instances")
class InstanceRestController(
    private val instanceService: InstanceService
) {

    @GetMapping("/{instanceId}/history")
    fun getHistory(@PathVariable instanceId: String): List<RestTemplateInstanceHistoryDto>? {
        return instanceService.getInstanceHistory(instanceId)
    }

    @GetMapping("/{instanceId}/comments")
    fun getComment(@PathVariable instanceId: String): List<InstanceCommentDto> {
        return instanceService.getComments(instanceId)
    }

    @PostMapping("/{instanceId}/comments")
    fun setComment(@PathVariable instanceId: String, @RequestBody contents: String): Boolean {
        return instanceService.setComment(instanceId, contents)
    }

    @DeleteMapping("/{instanceId}/comments/{commentId}")
    fun deleteComment(@PathVariable instanceId: String, @PathVariable commentId: String): Boolean {
        return instanceService.deleteComment(instanceId, commentId)
    }
}
