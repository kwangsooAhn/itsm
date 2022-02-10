/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.controller

import co.brainz.framework.tag.dto.AliceCustomTagDto
import co.brainz.framework.tag.service.AliceTagService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/tags")
class AliceTagRestController(
    private val aliceTagService: AliceTagService
) {

    @PostMapping("")
    fun setTag(@RequestBody aliceCustomTagDto: AliceCustomTagDto): String {
        return aliceTagService.insertTag(aliceCustomTagDto)
    }

    @DeleteMapping("/{tagId}")
    fun deleteTag(@PathVariable tagId: String): Boolean {
        aliceTagService.deleteTag(tagId)
        return true
    }

    @GetMapping("/whitelist")
    fun getSuggestionList(
        @RequestParam(value = "tagType", defaultValue = "") tagType: String,
        @RequestParam(value = "tagValue", defaultValue = "") tagValue: String
    ): List<String> {
        return aliceTagService.getSuggestionList(tagType, tagValue)
    }
}
