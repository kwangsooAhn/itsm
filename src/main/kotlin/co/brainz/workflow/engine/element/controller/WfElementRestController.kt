package co.brainz.workflow.engine.element.controller

import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.token.dto.WfActionDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/elements")
class WfElementRestController(private val wfActionService: WfActionService) {

    @GetMapping("/{elementId}")
    fun getElements(@PathVariable elementId: String): MutableList<WfActionDto> {
        return wfActionService.actions(elementId)
    }
}
