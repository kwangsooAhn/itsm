package co.brainz.workflow.engine.folder.controller

import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.engine.folder.service.WfFolderService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/folders")
class WfFolderController(private val wfFolderService: WfFolderService) {

    /*
     * Get related instance list
     */
    @GetMapping("")
    fun getRelatedInstanceList(@RequestParam tokenId: String): List<RestTemplateFolderDto> {
        return wfFolderService.getRelatedInstanceList(tokenId)
    }

    @PostMapping("")
    fun createFolderData(@RequestBody restTemplateFolderDto: List<RestTemplateFolderDto>) {
        return wfFolderService.createFolderData(restTemplateFolderDto)
    }

    @DeleteMapping("")
    fun deleteFolderData(@RequestBody restTemplateFolderDto: RestTemplateFolderDto) {
        return wfFolderService.deleteFolderData(restTemplateFolderDto)
    }
}
