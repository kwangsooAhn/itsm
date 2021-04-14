package co.brainz.framework.tag.controller

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.service.AliceTagService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/tags")
class AliceTagRestController(
    private val aliceTagService: AliceTagService
) {

    @PostMapping("")
    fun setTag(@RequestBody aliceTagDto: AliceTagDto): Boolean {
        return aliceTagService.insertTag(aliceTagDto)
    }

    @DeleteMapping("/{tagId}")
    fun deleteTag(@PathVariable tagId: String): Boolean {
        aliceTagService.deleteTag(tagId)
        return true
    }
}
