package co.brainz.workflow.engine.form.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.form.dto.WfFormComponentSaveDto
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.dto.WfFormDto
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional

@RestController
@RequestMapping("/rest/wf/forms")
class WfFormRestController(private val wfEngine: WfEngine) {

    @GetMapping("")
    fun getForms(@RequestParam parameters: LinkedHashMap<String, Any>): List<WfFormDto> {
        return wfEngine.form().forms(parameters)
    }

    @GetMapping("/{formId}")
    fun getForm(@PathVariable formId: String): WfFormComponentViewDto {
        return wfEngine.form().form(formId)
    }

    @PostMapping("")
    fun createForm(@RequestBody wfFormDto: WfFormDto): WfFormDto {
        return wfEngine.form().createForm(wfFormDto)
    }

    @Transactional
    @PutMapping("/{formId}")
    fun saveFormData(@RequestBody wfFormComponentSaveDto: WfFormComponentSaveDto, @PathVariable formId: String) {
        return wfEngine.form().saveForm(wfFormComponentSaveDto)
    }

    @Transactional
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String) {
        return wfEngine.form().deleteForm(formId)
    }

    @PostMapping("/{formId}")
    fun saveAsFormData(@RequestBody wfFormComponentSaveDto: WfFormComponentSaveDto, @PathVariable formId: String): WfFormDto {
        return wfEngine.form().saveAsForm(wfFormComponentSaveDto)
    }
}
