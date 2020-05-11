package co.brainz.workflow.engine.component.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.form.dto.WfFormComponentDataDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/components")
class WfComponentRestController(private val wfEngine: WfEngine) {

    /**
     * 컴포넌트 데이터를 조회한다
     */
    @GetMapping("/data")
    fun getComponentData(@RequestParam parameters: LinkedHashMap<String, Any>?): List<WfFormComponentDataDto> {
        return wfEngine.component().getComponentData(parameters)
    }
}
