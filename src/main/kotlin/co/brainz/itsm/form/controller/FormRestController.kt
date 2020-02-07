package co.brainz.itsm.form.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.dto.FormDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/rest/forms")
class FormRestController(private val formService: FormService) {

    /**
     * 문서양식 불러오기.
     */
    @GetMapping("/data/{formId}")
    fun getFormData(@PathVariable formId: String): String {
        // 테스트용 문서양식 데이터
        return """
               {
                "form": {"id": "$formId", "name": "장애신고", "description": "장애신고 신청서 문서양식입니다."},
                "components": [{
                    "id": "4a417b48-be2e-4ebe-82bf-8f80a63622a4",
                    "type": "text",
                    "label": {"position": "left", "column": 2, "size": 12, "color": "#ffffff", "bold": "Y", "italic": "N", "underline": "N", "text": "제목", "align": "left"},
                    "display": {"placeholder": "제목을 입력하세요.", "column": 10, "outline-width": 1, "outline-color": "#000000", "order": 1},
                    "validate": {"required": "N", "regexp": "", "regexp-msg": "", "length-min": 0, "length-max": 30}
                }]
               }
               """
    }

    /**
     * 문서양식 저장.
     */
    @PostMapping("/data")
    fun saveFormData(@RequestBody formData: String): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val formDto = FormDto(
                formName = "TEST111",
                formDesc = "TEST",
                formStatus = "form.status.edit",
                formEnabled = true,
                createDt = LocalDateTime.now(),
                createUserkey = aliceUserDto.userKey
        )
        formService.insertForm(formDto)
        return "1"
    }
}
