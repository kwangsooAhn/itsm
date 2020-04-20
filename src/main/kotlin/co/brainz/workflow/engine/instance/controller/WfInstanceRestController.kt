package co.brainz.workflow.engine.instance.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.instance.dto.WfInstanceCountDto
import co.brainz.workflow.engine.instance.dto.WfInstanceHistoryDto
import co.brainz.workflow.engine.instance.dto.WfInstanceViewDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/instances")
class WfInstanceRestController(private val wfEngine: WfEngine) {

    /**
     * Process Instance List.
     *
     * @param parameters
     * @return List<InstanceViewDto>
     */
    @GetMapping("")
    fun getProcessInstances(@RequestParam parameters: LinkedHashMap<String, Any>): List<WfInstanceViewDto> {
        return wfEngine.instance().instances(parameters)
    }

    /**
     * Process Instance.
     *
     * @param tokenId
     * @return WfInstanceViewDto
     */
    @GetMapping("/{tokenId}")
    fun getProcessInstance(@PathVariable tokenId: String): WfInstanceViewDto {
        return wfEngine.instance().instance(tokenId)
    }

    /**
     * Process Instance Status Count.
     *
     * @param param
     * @return List<WfInstanceCountDto>
     */
    @GetMapping("/count")
    fun getProcessInstancesStatusCount(@RequestParam param: LinkedHashMap<String, Any>): List<WfInstanceCountDto> {
        return wfEngine.instance().instancesStatusCount(param)
    }

    /**
     * Instance history.
     */
    @GetMapping("/{instanceId}/history")
    fun getInstancesHistory(@PathVariable instanceId: String): List<WfInstanceHistoryDto> {
        return wfEngine.instance().getInstancesHistory(instanceId)
    }
}