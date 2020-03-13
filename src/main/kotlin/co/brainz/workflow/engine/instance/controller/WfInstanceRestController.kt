package co.brainz.workflow.engine.instance.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.instance.dto.WfTicketDto
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
    fun getProcessInstances(@RequestParam parameters: LinkedHashMap<String, Any>): List<WfTicketDto> {
        return wfEngine.instance().instances(parameters)
    }

    /**
     * Process Instance.
     *
     * @param tokenId
     * @return TicketDto
     */
    @GetMapping("/{tokenId}")
    fun getProcessInstance(@PathVariable tokenId: String): WfTicketDto {
        return wfEngine.instance().instance(tokenId)
    }

}
