package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormAdminService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/forms-admin")
class FormRestAdminController(private val formAdminService: FormAdminService) {

    /**
     * 문서양식 등록, 새이름으로 저장
     */
    @PostMapping("")
    fun createForm(
        @RequestParam(value = "saveType", defaultValue = "") saveType: String,
        @RequestBody jsonData: Any
    ): String {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return when (saveType) {
            RestTemplateConstants.FormSaveType.SAVE_AS.code ->
                formAdminService.saveAsForm(mapper.writeValueAsString(jsonData))
            else -> formAdminService.createForm(mapper.convertValue(jsonData, RestTemplateFormDto::class.java))
        }
    }

    /**
     * 폼 리스트 조회 (스크롤).
     */
    @GetMapping("")
    fun getFormList(request: HttpServletRequest, model: Model): List<RestTemplateFormDto> {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search") ?: ""
        params["offset"] = request.getParameter("offset") ?: "0"
        return formAdminService.findForms(params)
    }

    /**
     * 문서양식 저장.
     */
    @PutMapping("/{formId}")
    fun updateProcess(
        @RequestBody restTemplateFormDto: RestTemplateFormDto,
        @PathVariable formId: String
    ): Boolean {
        return formAdminService.updateFrom(formId, restTemplateFormDto)
    }
}
