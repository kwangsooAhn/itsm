package co.brainz.workflow.form.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.form.dto.FormComponentSaveDto
import co.brainz.workflow.form.dto.FormComponentViewDto
import co.brainz.workflow.form.dto.FormDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@RestController
@RequestMapping("/rest/wf/forms")
class WFFormRestController(private val wfEngine: WFEngine) {

    @GetMapping("")
    fun getForms(request: HttpServletRequest): List<FormDto> {
        return wfEngine.form().forms(request.getParameter("search") ?: "")
    }

    @GetMapping("/{formId}")
    fun getForm(@PathVariable formId: String): FormComponentViewDto {
        return wfEngine.form().form(formId)
    }

    @PostMapping("")
    fun createForm(@RequestBody formDto: FormDto): FormDto {
        return wfEngine.form().createForm(formDto)
    }

    @Transactional
    @PutMapping("/{formId}")
    fun saveFormData(@RequestBody formComponentSaveDto: FormComponentSaveDto, @PathVariable formId: String) {
        return wfEngine.form().saveForm(formComponentSaveDto)
    }

    @Transactional
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String) {
        return wfEngine.form().deleteForm(formId)
    }

}
