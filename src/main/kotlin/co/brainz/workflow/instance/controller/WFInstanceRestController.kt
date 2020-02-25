package co.brainz.workflow.instance.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.instance.dto.InstanceDto
import com.google.gson.Gson
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/instances")
class WFInstanceRestController(private val wfEngine: WFEngine) {

    //test
    @GetMapping("/create")
    fun createInstance() {

        val instanceDto = InstanceDto(
                instanceId = "",
                processId = "abcdedfdddd"
        )
        println(wfEngine.instance().createInstance(instanceDto))

    }

    @GetMapping("/complete/{instanceId}")
    fun completeInstance(@PathVariable instanceId: String) {
        val instanceDto = InstanceDto(
                instanceId = instanceId,
                processId = "abcdedfdddd"
        )
        wfEngine.instance().completeInstance(instanceDto)

    }

}
