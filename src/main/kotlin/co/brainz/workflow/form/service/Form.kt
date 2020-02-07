package co.brainz.workflow.form.service

import co.brainz.workflow.form.dto.FormDto
import org.springframework.util.LinkedMultiValueMap

interface Form {

    /**
     * 폼 리스트 조회.
     */
    fun formList(search: String): List<FormDto>

    /**
     * 폼 데이터 조회.
     */
    fun form(formId: String): FormDto

    /**
     * 폼 등록.
     */
    //fun insertForm(formDto: FormDto)
    fun insertForm(multiValueMap: LinkedMultiValueMap<String, Any>)

    /**
     * 폼 수정.
     */
    fun updateForm(formDto: FormDto)

    /**
     * 폼 삭제.
     */
    fun deleteForm(formId: String)

}
