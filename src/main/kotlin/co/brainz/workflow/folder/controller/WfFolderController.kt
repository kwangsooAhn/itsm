package co.brainz.workflow.folder.controller

import co.brainz.workflow.folder.service.WfFolderService
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/folders")
class WfFolderController(
    private val wfFolderService: WfFolderService
) {

    /*
     * Get related instance list
     */
    @GetMapping("")
    fun getRelatedInstanceList(@RequestParam tokenId: String): List<RestTemplateRelatedInstanceDto> {
        return wfFolderService.getRelatedInstanceList(tokenId)
    }

    @GetMapping("/{tokenId}")
    fun getOriginFolder(@PathVariable tokenId: String): RestTemplateFolderDto {
        return wfFolderService.getOriginFolder(tokenId)
    }

    @PostMapping("")
    fun createFolderData(@RequestBody restTemplateFolderDto: List<RestTemplateFolderDto>) {
        return wfFolderService.createFolderData(restTemplateFolderDto)
    }

    @DeleteMapping("/{folderId}")
    fun deleteFolderData(@PathVariable folderId: String, @RequestBody restTemplateFolderDto: RestTemplateFolderDto) {
        return wfFolderService.deleteFolderData(folderId, restTemplateFolderDto)
    }
}
