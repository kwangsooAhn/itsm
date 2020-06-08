package co.brainz.workflow.engine.tag.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.provider.dto.RestTemplateTagDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/tags")
class WfTagRestController(
    private val wfEngine: WfEngine
) {

    @PostMapping("")
    fun insertTag(@RequestBody restTemplateTagDto: RestTemplateTagDto): Boolean {
        return wfEngine.tag().insertTag(restTemplateTagDto)
    }

    @DeleteMapping("/{tagId}")
    fun deleteTag(@PathVariable tagId: String): Boolean {
        return wfEngine.tag().deleteTag(tagId)
    }
}
