package co.brainz.itsm.tag.controller

import co.brainz.itsm.tag.service.TagService
import co.brainz.workflow.provider.dto.RestTemplateTagDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/tags")
class TagRestController(
    private val tagService: TagService
) {

    @PostMapping("")
    fun setComment(@RequestBody restTemplateTagDto: RestTemplateTagDto): Boolean {
        return tagService.setTag(restTemplateTagDto)
    }

    @DeleteMapping("/{tagId}")
    fun deleteComment(@PathVariable tagId: String): ResponseEntity<String> {
        return tagService.deleteTag(tagId)
    }
}
