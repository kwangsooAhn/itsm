package co.brainz.workflow.engine.form.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.form.dto.WfFormComponentDataDto
import co.brainz.workflow.engine.form.constants.WfFormConstants
import co.brainz.workflow.engine.form.dto.WfFormComponentSaveDto
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.dto.WfFormDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/forms")
class WfFormRestController(private val wfEngine: WfEngine) {

    @GetMapping("")
    fun getForms(@RequestParam parameters: LinkedHashMap<String, Any>): List<WfFormDto> {
        return wfEngine.form().forms(parameters)
    }

    @GetMapping("/{formId}")
    fun getForm(@PathVariable formId: String): WfFormDto {
        return wfEngine.form().form(formId)
    }

    @GetMapping("/{formId}/data")
    fun getFormData(@PathVariable formId: String): WfFormComponentViewDto {
        return wfEngine.form().formData(formId)
    }

    @PostMapping("")
    fun createForm(
        @RequestParam(value = "saveType", defaultValue = "") saveType: String,
        @RequestBody jsonData: Any
    ): WfFormDto {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return when (saveType) {
            WfFormConstants.FormSaveType.SAVE_AS.value -> wfEngine.form().saveAsFormData(mapper.convertValue(jsonData, WfFormComponentSaveDto::class.java))
            else -> wfEngine.form().createForm(mapper.convertValue(jsonData, WfFormDto::class.java))
        }
    }

    @Transactional
    @PutMapping("/{formId}")
    fun updateForm(@RequestBody wfFormDto: WfFormDto, @PathVariable formId: String): Boolean {
        return wfEngine.form().updateForm(wfFormDto)
    }

    @Transactional
    @PutMapping("/{formId}/data")
    fun saveFormData(@RequestBody wfFormComponentSaveDto: WfFormComponentSaveDto, @PathVariable formId: String) {
        return wfEngine.form().saveFormData(wfFormComponentSaveDto)
    }

    @Transactional
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String) {
        return wfEngine.form().deleteForm(formId)
    }

    @GetMapping("/components")
    fun getFormComponentData(request: HttpServletRequest): List<WfFormComponentDataDto> {
        return wfEngine.form().getFormComponentData(request.getParameter("componentType") ?: "")
    }
}
