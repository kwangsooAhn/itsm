package co.brainz.workflow.instance.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.instance.dto.TicketDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/wf/instances")
class WFInstanceController(private val wfEngine: WFEngine) {

    @GetMapping("")
    fun getProcessInstances(request: HttpServletRequest): List<TicketDto> {
        //TODO request를 풀면서 key, value 값을 추출한다.
        //map 에 담아서 전달한다.
        return wfEngine.instance().instances()
    }

    @GetMapping("/{instanceId}")
    fun getProcessInstance(@PathVariable instanceId: String) {
        return wfEngine.instance().instance(instanceId)
    }
}
