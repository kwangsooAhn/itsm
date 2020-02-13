package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.provider.ProviderForm
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.ComponentDto
import co.brainz.itsm.provider.dto.FormComponentDto
import co.brainz.itsm.provider.dto.FormDto
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

    fun findForm(formId: String): FormDto {
        val responseBody = providerForm.getForm(formId)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val formDto = mapper.readValue(responseBody, FormDto::class.java)
        formDto.createDt = formDto.createDt?.let { ProviderUtilities().toTimezone(it) }
        formDto.updateDt = formDto.updateDt?.let { ProviderUtilities().toTimezone(it) }

        return formDto
    }

    fun findFormComponents(formId: String): String {
        return providerForm.getFormComponents(formId)
    }

    //Dto를 사용하여 처리할 경우 사용 (예시)
    fun findFormComponentsObject(formId: String): FormComponentDto {
        val responseBody = providerForm.getFormComponents(formId)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val dataMap = mapper.readValue(responseBody, Map::class.java)
        val formComponentDto = FormComponentDto(form = FormViewDto())
        if (dataMap.containsKey("form")) {
            formComponentDto.form = mapper.convertValue(dataMap["form"], FormViewDto::class.java)
            if (dataMap.containsKey("components")) {
                val components: MutableList<ComponentDto> = mapper.convertValue(dataMap["components"], mapper.typeFactory.constructCollectionType(MutableList::class.java, ComponentDto::class.java))
                formComponentDto.components = components
            }
        }

        return formComponentDto
    }

    fun deleteForm(formId: String) {
        providerForm.deleteForm(formId)
    }

    fun insertForm(formDto: FormDto): String {
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

    fun updateForm(formDto: FormDto) {
        formDto.createDt = formDto.createDt?.let { ProviderUtilities().toGMT(it) }
        formDto.updateDt = formDto.updateDt?.let { ProviderUtilities().toGMT(it) }
        providerForm.putForm(formDto)
    }

}
