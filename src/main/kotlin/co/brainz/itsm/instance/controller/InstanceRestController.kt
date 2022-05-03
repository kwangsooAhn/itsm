/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.instance.dto.CommentDto
import co.brainz.itsm.instance.dto.InstanceViewerListDto
import co.brainz.itsm.instance.service.InstanceService
import org.springframework.http.ResponseEntity
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
    fun getHistory(@PathVariable instanceId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(instanceService.getInstanceHistory(instanceId))
    }

    @GetMapping("/{instanceId}/comments")
    fun getComment(@PathVariable instanceId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(instanceService.getComments(instanceId))
    }

    @PostMapping("/{instanceId}/comments")
    fun setComment(
        @PathVariable instanceId: String,
        @RequestBody commentDto: CommentDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(instanceService.setComment(instanceId, commentDto))
    }

    @DeleteMapping("/{instanceId}/comments/{commentId}")
    fun deleteComment(
        @PathVariable instanceId: String,
        @PathVariable commentId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(instanceService.deleteComment(instanceId, commentId))
    }

    @GetMapping("/{instanceId}/tags")
    fun getTag(@PathVariable instanceId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(instanceService.getInstanceTags(instanceId))
    }

    @GetMapping("/{instanceId}/viewer/")
    fun getInstanceViewerList(@PathVariable instanceId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(instanceService.getInstanceViewerList(instanceId))
    }

    @PostMapping("/{instanceId}/viewer/")
    fun createInstanceViewerDto(
        @PathVariable instanceId: String,
        @RequestBody instanceViewerListDto: InstanceViewerListDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            instanceService.createInstanceViewer(instanceId, instanceViewerListDto)
        )
    }

    @DeleteMapping("/{instanceId}/viewer/{viewerKey}")
    fun deleteInstanceViewer(
        @PathVariable instanceId: String,
        @PathVariable viewerKey: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(instanceService.deleteInstanceViewer(instanceId, viewerKey))
    }

}
