package co.brainz.workflow.component.contorller

import co.brainz.workflow.component.dto.ComponentDto
import co.brainz.workflow.engine.WFEngine
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/wf/components")
class WFComponentRestController(private val wfEngine: WFEngine) {

    @GetMapping("")
    fun getComponents(request: HttpServletRequest): String {
        return wfEngine.component().getComponents(request.getParameter("search") ?: "")

    }

    @GetMapping("/{compId}")
    fun getComponent(@PathVariable compId: String): ComponentDto {
        return wfEngine.component().getComponent(compId)
    }



}
