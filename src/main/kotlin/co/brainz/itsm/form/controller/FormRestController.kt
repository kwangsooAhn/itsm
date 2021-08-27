/**
 * Form RestAPI 처리용 컨트롤러
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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

    /**
     * 문서양식 일반정보 조회
     *
     * @param formId
     * @return RestTemplateFormDto
     */
    @GetMapping("/{formId}")
    fun getForm(@PathVariable formId: String): RestTemplateFormDto {
        return formService.getForm(formId)
    }

    /**
     * 문서양식 전체 데이터 조회.
     *
     * @param formId
     * @return RestTemplateFormDataDto
     */
    @GetMapping("/{formId}/data")
    fun getFormData(@PathVariable formId: String): RestTemplateFormDataDto {
        return formService.getFormData(formId)
    }

    /**
     * 신규 문서양식 등록, 새이름으로 저장
     *
     * @param saveType
     * @param jsonData
     * @return String
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
                formService.saveAsForm(mapper.writeValueAsString(jsonData))
            else -> formService.createForm(mapper.convertValue(jsonData, RestTemplateFormDto::class.java))
        }
    }

    /**
     * 문서양식 일반정보 저장.
     *
     * @param restTemplateFormDto
     * @param formId
     * @return Boolean
     */
    @PutMapping("/{formId}")
    fun saveForm(
        @RequestBody restTemplateFormDto: RestTemplateFormDto,
        @PathVariable formId: String
    ): Boolean {
        return formService.saveForm(formId, restTemplateFormDto)
    }

    /**
     * 문서양식 전체 데이터 저장.
     *
     * @param formData
     * @param formId
     * @return Boolean
     */
    @PutMapping("/{formId}/data")
    fun saveFormData(@RequestBody formData: RestTemplateFormDataDto, @PathVariable formId: String): Boolean {
        return formService.saveFormData(formId, formData)
    }

    /**
     * 문서 삭제.
     *
     * @param formId
     * @return Boolean
     */
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String): Boolean {
        return formService.deleteForm(formId)
    }
}
