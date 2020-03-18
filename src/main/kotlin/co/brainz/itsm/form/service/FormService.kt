package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.itsm.form.dto.FormComponentDataDto
import co.brainz.itsm.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateFormComponentSaveDto
import co.brainz.workflow.provider.dto.RestTemplateFormSaveDto
import co.brainz.workflow.provider.dto.RestTemplateFormViewDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import java.time.LocalDateTime

@Service
class FormService(private val restTemplate: RestTemplateProvider) {

    fun findForms(search: String): List<RestTemplateFormDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.GET_FORMS.url, parameters = params)
        val responseBody = restTemplate.get(urlDto)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val forms: List<RestTemplateFormDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateFormDto::class.java))
        for (form in forms) {
            form.createDt = form.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
            form.updateDt = form.updateDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return forms
    }

    fun findForm(formId: String): String {
        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.GET_FORM.url.replace(restTemplate.getKeyRegex(), formId))
        return restTemplate.get(urlDto)
    }

    fun createForm(restTemplateFormDto: RestTemplateFormDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateFormDto.formStatus = RestTemplateConstants.FormStatus.EDIT.value
        restTemplateFormDto.createUserKey = aliceUserDto.userKey
        restTemplateFormDto.createDt =  AliceTimezoneUtils().toGMT(LocalDateTime.now())
        restTemplateFormDto.updateDt = restTemplateFormDto.updateDt?.let { AliceTimezoneUtils().toGMT(it) }
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.POST_FORM.url)
        val responseBody = restTemplate.create(url, restTemplateFormDto)
        return when (responseBody.isNotEmpty()) {
            true -> {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                val dataDto = mapper.readValue(responseBody, RestTemplateFormDto::class.java)
                dataDto.formId
            }
            false -> ""
        }
    }

    fun deleteForm(formId: String): Boolean {
        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.DELETE_FORM.url.replace(restTemplate.getKeyRegex(), formId))
        return restTemplate.delete(urlDto)
    }

    fun saveFormData(formData: String): Boolean {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val map = mapper.readValue(formData, LinkedHashMap::class.java)
        val forms = mapper.convertValue(map["form"], RestTemplateFormViewDto::class.java)
        val components:MutableList<LinkedHashMap<String, Any>>  = mapper.convertValue(map["components"], TypeFactory.defaultInstance().constructCollectionType(MutableList::class.java, LinkedHashMap::class.java))

        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val formSaveDto = RestTemplateFormSaveDto(
                formId = forms.id,
                formName = forms.name,
                formDesc = forms.desc,
                formStatus = forms.status,
                updateDt = AliceTimezoneUtils().toGMT(LocalDateTime.now()),
                updateUserKey = aliceUserDto.userKey
        )
        val formComponentSaveDto = RestTemplateFormComponentSaveDto(
                form = formSaveDto,
                components = components
        )
        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.PUT_FORM.url.replace(restTemplate.getKeyRegex(), formComponentSaveDto.form.formId))
        return restTemplate.update(urlDto, formComponentSaveDto)
    }

    fun getFormComponentData(componentType: String, attributeId: String): List<FormComponentDataDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("attributeId", attributeId)
        val urlDto = RestTemplateUrlDto(
                callUrl = RestTemplateConstants.Form.GET_FORM_COMPONENT_DATA.url.replace(restTemplate.getKeyRegex(), componentType),
                parameters = params)
        val responseBody = restTemplate.get(urlDto)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, FormComponentDataDto::class.java))
    }
}