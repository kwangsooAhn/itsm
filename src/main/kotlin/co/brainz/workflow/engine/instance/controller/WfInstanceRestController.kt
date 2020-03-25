package co.brainz.workflow.engine.instance.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.instance.dto.WfInstanceViewDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

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
     * Process Instance Status Count.
     *
     * @param request
     * @return List<Map<String, Any>>
     */
    @GetMapping("/count")
    fun getProcessInstancesStatusCount(request: HttpServletRequest): List<Map<String, Any>> {
        return wfEngine.instance().instancesStatusCount(request.getParameter("userKey") ?: "")
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
}
