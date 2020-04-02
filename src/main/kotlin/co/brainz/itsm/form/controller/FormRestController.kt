package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.constants.RestTemplateConstants
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.ResponseEntity
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
@RequestMapping("/rest/forms")
class FormRestController(private val formService: FormService) {

    @PostMapping("")
    fun createForm(@RequestParam(value = "saveType", defaultValue = "") saveType: String,
                   @RequestBody jsonData: Any): String {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return when (saveType) {
            RestTemplateConstants.FormSaveType.SAVE_AS.code -> formService.saveAsForm(mapper.writeValueAsString(jsonData))
            else -> formService.createForm(mapper.convertValue(jsonData, RestTemplateFormDto::class.java))
        }
    }

    /**
     * 문서양식 불러오기.
     */
    @GetMapping("/data/{formId}")
    fun getFormData(@PathVariable formId: String): String {
        return formService.getFormData(formId)
    }

    /**
     * 문서양식 저장.
     */
    @PutMapping("/data")
    fun saveFormData(@RequestBody formData: String): Boolean {
        return formService.saveFormData(formData)
    }

    /**
     * 문서 삭제.
     */
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String): ResponseEntity<String> {
        return formService.deleteForm(formId)
    }

}
