package co.brainz.workflow.instance.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.instance.dto.TicketViewDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/wf/instances")
class WFInstanceRestController(private val wfEngine: WFEngine) {

    /**
     * Process Instance List.
     *
     * @param request
     * @return List<InstanceViewDto>
     */
    @GetMapping("")
    fun getProcessInstances(request: HttpServletRequest): List<TicketViewDto> {
        return wfEngine.instance().getInstances(request.getParameter("userKey"), request.getParameter("status"));
    }
}