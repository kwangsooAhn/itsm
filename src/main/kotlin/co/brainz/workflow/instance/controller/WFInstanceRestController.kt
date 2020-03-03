package co.brainz.workflow.instance.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.instance.dto.TicketDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/instances")
class WFInstanceRestController(private val wfEngine: WFEngine) {

    /**
     * Process Instance List.
     *
     * @param parameters
     * @return List<InstanceViewDto>
     */
    @GetMapping("")
    fun getProcessInstances(@RequestParam parameters: LinkedHashMap<String, Any>): List<TicketDto> {
        return wfEngine.instance().instances(parameters)
    }

    /**
     * Process Instance.
     *
     * @param tokenId
     * @return TicketDto
     */
    @GetMapping("/{tokenId}")
    fun getProcessInstance(@PathVariable tokenId: String): TicketDto {
        return wfEngine.instance().instance(tokenId)
    }

}
