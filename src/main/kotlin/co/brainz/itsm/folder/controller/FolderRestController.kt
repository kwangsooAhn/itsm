package co.brainz.itsm.folder.controller

import co.brainz.itsm.folder.service.FolderService
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import org.springframework.http.ResponseEntity
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

  /*  @PostMapping("")
    fun createFolder(@RequestBody restTemplateFolderDto: List<RestTemplateFolderDto>): Boolean {
        return folderService.createFolder(restTemplateFolderDto)
    }

    @DeleteMapping("")
    fun deleteFolder(@RequestBody restTemplateFolderDto: RestTemplateFolderDto): ResponseEntity<Map<String, Any>> {
        return folderService.deleteFolder(restTemplateFolderDto)
    }*/
}
