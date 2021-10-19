package co.brainz.itsm.folder.controller

import co.brainz.itsm.folder.dto.FolderDto
import co.brainz.itsm.folder.service.FolderService
import org.springframework.web.bind.annotation.DeleteMapping
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

    @PostMapping("")
    fun insertFolderDto(@RequestBody folderDto: FolderDto): Boolean {
        return folderService.insertFolderDto(folderDto)
    }

    @DeleteMapping("/{folderId}")
    fun deleteFolder(
        @PathVariable folderId: String,
        @RequestBody folderDto: FolderDto
    ): Boolean {
        return folderService.deleteInstanceInFolder(folderId, folderDto)
    }
}
