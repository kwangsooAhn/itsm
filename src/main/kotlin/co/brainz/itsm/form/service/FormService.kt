package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.provider.ProviderForm
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.FormDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import java.time.LocalDateTime

@Service
class FormService(private val providerForm: ProviderForm) {

    fun findFormList(search: String): List<FormDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val responseBody = providerForm.wfGetFormList(params)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val formList: List<FormDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, FormDto::class.java))
        for (item in formList) {
            item.createDt = item.createDt?.let { ProviderUtilities().toTimezone(it) }
            item.updateDt = item.updateDt?.let { ProviderUtilities().toTimezone(it) }
        }

        return formList
    }

    fun findForm(formId: String): FormDto {
        val responseBody = providerForm.wfGetForm(formId)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val formDto = mapper.readValue(responseBody, FormDto::class.java)
        formDto.createDt = formDto.createDt?.let { ProviderUtilities().toTimezone(it) }
        formDto.updateDt = formDto.updateDt?.let { ProviderUtilities().toTimezone(it) }

        return formDto
    }

    fun deleteForm(formId: String) {
        providerForm.wfDeleteForm(formId)
    }

    fun insertForm(formDto: FormDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        formDto.formStatus = ProviderConstants.FormStatus.EDIT.value
        formDto.createUserkey = aliceUserDto.userKey
        formDto.createDt =  ProviderUtilities().toGMT(LocalDateTime.now())
        formDto.updateDt = formDto.updateDt?.let { ProviderUtilities().toGMT(it) }
        val responseBody: String = providerForm.wfPostForm(formDto)
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
        providerForm.wfPutForm(formDto)
    }

}
