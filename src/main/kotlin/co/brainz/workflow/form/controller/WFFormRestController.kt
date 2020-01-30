package co.brainz.workflow.form.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.form.dto.FormDto
import co.brainz.workflow.form.repository.FormRepository
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
class WFFormRestController(private val formRepository: FormRepository) {

    @GetMapping("")
    fun getFormList(request: HttpServletRequest): List<FormDto> {
        return WFEngine().form(formRepository).formList(request.getParameter("search") ?: "")
    }

    @GetMapping("/{formId}")
    fun getForm(@PathVariable formId: String): FormDto {
        return WFEngine().form(formRepository).form(formId)
    }

    @PostMapping("")
    fun insertForm(@RequestBody formDto: FormDto) {
        return WFEngine().form(formRepository).insertForm(formDto)
    }

    @PutMapping("/{formId}")
    fun updateForm(@RequestBody formDto: FormDto, @PathVariable formId: String) {
        return WFEngine().form(formRepository).updateForm(formDto)
    }

    @Transactional
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String) {
        return WFEngine().form(formRepository).deleteForm(formId)
    }

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
                    "display": {"placeholder": "제목을 입력하세요.", "column": 10, "outline-width": 1, "outline-color": "#000000", order: 1},
                    "validate": {"required": "N", "regexp": "", "regexp-msg": "", "length-min": 0, "length-max": 30}
                }]
               }
               """
    }

    @PostMapping("/data")
    fun saveFormData(@RequestBody formData: String): String {
        // 테스트용 데이터
        println(formData)
        return "1"
    }

}
