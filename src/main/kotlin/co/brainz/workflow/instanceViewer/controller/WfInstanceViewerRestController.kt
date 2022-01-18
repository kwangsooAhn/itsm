package co.brainz.workflow.instanceViewer.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.workflow.instanceViewer.dto.InstanceViewerDto
import co.brainz.workflow.instanceViewer.service.WfInstanceViewerService

@RestController
@RequestMapping("/rest/instances")
class WfInstanceViewerRestController(
    private val wfInstanceViewerService: WfInstanceViewerService
) {

    @PostMapping("{instanceId}/viewer/")
    fun createInstanceViewerDto(@RequestBody instanceViewerDto: InstanceViewerDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(wfInstanceViewerService.createInstanceViewer(instanceViewerDto))
    }

    @DeleteMapping("{instanceId}/viewer/{viewerKey}")
    fun deleteInstanceViewer(@RequestBody instanceId: String, viewerKey: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(wfInstanceViewerService.deleteInstanceViewer(instanceId, viewerKey))
    }
}