/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.folder.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.folder.dto.InstanceFolderListDto
import co.brainz.itsm.folder.service.FolderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/folders")
class FolderRestController(
    private val folderService: FolderService
) {

    @GetMapping("{folderId}")
    fun getFolder(@PathVariable folderId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(folderService.getFolder(folderId))
    }

    @PostMapping("")
    fun insertFolderDto(
        @RequestBody instanceFolderListDto: InstanceFolderListDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(folderService.insertFolderDto(instanceFolderListDto))
    }

    @DeleteMapping("/{folderId}/instances/{instanceId}")
    fun deleteFolder(
        @PathVariable folderId: String,
        @PathVariable instanceId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            folderService.deleteInstanceInFolder(folderId, instanceId)
        )
    }
}
