package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.provider.ProviderForm
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.FormComponentSaveDto
import co.brainz.itsm.provider.dto.FormDto
import co.brainz.itsm.provider.dto.FormSaveDto
import co.brainz.itsm.provider.dto.FormViewDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import java.time.LocalDateTime

@Service
class FormService(private val providerForm: ProviderForm) {

    fun findForms(search: String): List<FormDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val responseBody = providerForm.getForms(params)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val forms: List<FormDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, FormDto::class.java))
        for (form in forms) {
            form.createDt = form.createDt?.let { ProviderUtilities().toTimezone(it) }
            form.updateDt = form.updateDt?.let { ProviderUtilities().toTimezone(it) }
        }

        return forms
    }

    fun findForm(formId: String): String {
        return providerForm.getForm(formId)
    }

    fun createForm(formDto: FormDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        formDto.formStatus = ProviderConstants.FormStatus.EDIT.value
        formDto.createUserKey = aliceUserDto.userKey
        formDto.createDt =  ProviderUtilities().toGMT(LocalDateTime.now())
        formDto.updateDt = formDto.updateDt?.let { ProviderUtilities().toGMT(it) }
        val responseBody: String = providerForm.postForm(formDto)
        return when (responseBody.isNotEmpty()) {
            true -> {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                val dataDto = mapper.readValue(responseBody, FormDto::class.java)
                dataDto.formId
            }
            false -> ""
        }
    }

    fun deleteForm(formId: String): Boolean {
        return providerForm.deleteForm(formId)
    }

    fun saveFormData(formData: String): Boolean {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val map = mapper.readValue(formData, LinkedHashMap::class.java)
        val forms = mapper.convertValue(map["form"], FormViewDto::class.java)
        val collections: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(map["collections"], mapper.typeFactory.constructCollectionType(MutableList::class.java, LinkedHashMap::class.java))

        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val formSaveDto = FormSaveDto(
                formId = forms.id,
                formName = forms.name,
                formDesc = forms.desc,
                updateDt = ProviderUtilities().toGMT(LocalDateTime.now()),
                updateUserKey = aliceUserDto.userKey
        )
        val formComponentSaveDto = FormComponentSaveDto(
                form = formSaveDto,
                components = collections
        )

        return providerForm.putForm(formComponentSaveDto)
    }

}
