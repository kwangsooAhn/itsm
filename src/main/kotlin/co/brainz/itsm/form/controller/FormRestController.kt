package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/rest/forms")
class FormRestController(
    private val formService: FormService
) {

    @PostMapping("")
    fun createForm(
        @RequestParam(value = "saveType", defaultValue = "") saveType: String,
        @RequestBody jsonData: Any
    ): String {
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
    @GetMapping("/{formId}/data")
    fun getFormData(@PathVariable formId: String): String {
        return formService.getFormData(formId)
    }

    /**
     * 문서양식 저장.
     */
    @PutMapping("/{formId}/data")
    fun saveFormData(@RequestBody formData: String, @PathVariable formId: String): Boolean {
        return formService.saveFormData(formId, formData)
    }

    /**
     * 문서 삭제.
     */
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String): ResponseEntity<String> {
        return formService.deleteForm(formId)
    }

    /**
     * 이미지 컴포넌트 이미지 파일 업로드.
     */
    @PostMapping("/imageUpload")
    fun uploadFile(@RequestPart("file") multipartFile: MultipartFile): ResponseEntity<Map<String, Any>> {
        val response: ResponseEntity<Map<String, Any>>
        val map: MutableMap<String, Any> = mutableMapOf()
        // TODO resoures/public/assets/media/image/form 경로에 파일업로드
        // map["file"] = formService.upload(multipartFile)

        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json; charset=utf-8")
        response = ResponseEntity(map, headers, HttpStatus.OK)

        return response
    }
}
