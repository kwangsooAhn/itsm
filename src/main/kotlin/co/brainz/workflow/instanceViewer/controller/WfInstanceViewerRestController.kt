package co.brainz.workflow.instanceViewer.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.workflow.instanceViewer.dto.WfInstanceViewerDataDto
import co.brainz.workflow.instanceViewer.dto.WfInstanceViewerListReturnDto
import co.brainz.workflow.instanceViewer.service.WfInstanceViewerService
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
class WfInstanceViewerRestController(
    private val wfInstanceViewerService: WfInstanceViewerService
) {
    @GetMapping("/{instanceId}/viewer/")
    fun getInstanceViewerList(@PathVariable instanceId: String): WfInstanceViewerListReturnDto? {
        return wfInstanceViewerService.getInstanceViewerList(instanceId)
    }

    //참조인 등록
    @PostMapping("/{instanceId}/viewer/")
    fun createInstanceViewerDto(
        @RequestBody wfInstanceViewerDataDtoList: List<WfInstanceViewerDataDto>, @PathVariable instanceId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            wfInstanceViewerService.createInstanceViewer(wfInstanceViewerDataDtoList, instanceId)
        )
    }

    //참조인 삭제
    @DeleteMapping("/{instanceId}/viewer/{viewerKey}")
    fun deleteInstanceViewer(
        @PathVariable instanceId: String, @PathVariable viewerKey: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(wfInstanceViewerService.deleteInstanceViewer(instanceId, viewerKey))
    }
}
