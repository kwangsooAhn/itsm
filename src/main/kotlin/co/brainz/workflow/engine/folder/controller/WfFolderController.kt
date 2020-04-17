package co.brainz.workflow.engine.folder.controller

import co.brainz.workflow.engine.folder.dto.WfFolderDto
import co.brainz.workflow.engine.folder.service.WfFolderService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/wf/folders")
class WfFolderController(private val wfFolderService: WfFolderService) {

    /*
     * Get related instance list
     */
    @GetMapping("")
    fun getRelatedInstanceList(@RequestParam tokenId: String): List<WfFolderDto> {
        return wfFolderService.getRelatedInstanceList(tokenId)
    }
}