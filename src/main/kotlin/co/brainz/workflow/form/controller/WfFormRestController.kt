package co.brainz.workflow.form.controller

import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.dto.RestTemplateFormComponentSaveDto
import co.brainz.workflow.provider.dto.RestTemplateFormComponentViewDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
class WfFormRestController(
    private val wfFormService: WfFormService
) {

    @GetMapping("")
    fun getForms(@RequestParam parameters: LinkedHashMap<String, Any>): List<RestTemplateFormDto> {
        return wfFormService.forms(parameters)
    }

    @GetMapping("/{formId}")
    fun getForm(@PathVariable formId: String): RestTemplateFormDto {
        return wfFormService.form(formId)
    }

    @GetMapping("/{formId}/data")
    fun getFormData(@PathVariable formId: String): RestTemplateFormComponentViewDto {
        return wfFormService.formData(formId)
    }

    @PostMapping("")
    fun createForm(
        @RequestParam(value = "saveType", defaultValue = "") saveType: String,
        @RequestBody jsonData: Any
    ): RestTemplateFormDto {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return when (saveType) {
            WfFormConstants.FormSaveType.SAVE_AS.value -> wfFormService
                .saveAsFormData(mapper.convertValue(jsonData, RestTemplateFormComponentSaveDto::class.java))
            else -> wfFormService.createForm(mapper.convertValue(jsonData, RestTemplateFormDto::class.java))
        }
    }

    @Transactional
    @PutMapping("/{formId}")
    fun updateForm(@RequestBody restTemplateFormDto: RestTemplateFormDto, @PathVariable formId: String): Boolean {
        return wfFormService.updateForm(restTemplateFormDto)
    }

    @Transactional
    @PutMapping("/{formId}/data")
    fun saveFormData(
        @RequestBody restTemplateFormComponentSaveDto: RestTemplateFormComponentSaveDto,
        @PathVariable formId: String
    ) {
        return wfFormService.saveFormData(restTemplateFormComponentSaveDto)
    }

    @Transactional
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String): Boolean {
        return wfFormService.deleteForm(formId)
    }

}
