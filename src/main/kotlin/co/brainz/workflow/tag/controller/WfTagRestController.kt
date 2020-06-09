package co.brainz.workflow.tag.controller

import co.brainz.workflow.provider.dto.RestTemplateTagDto
import co.brainz.workflow.tag.service.WfTagService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/tags")
class WfTagRestController(
    private val wfTagService: WfTagService
) {

    @PostMapping("")
    fun insertTag(@RequestBody restTemplateTagDto: RestTemplateTagDto): Boolean {
        return wfTagService.insertTag(restTemplateTagDto)
    }

    @DeleteMapping("/{tagId}")
    fun deleteTag(@PathVariable tagId: String): Boolean {
        return wfTagService.deleteTag(tagId)
    }
}
