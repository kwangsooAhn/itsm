package co.brainz.itsm.form.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.provider.ProviderForm
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.dto.FormDto
import co.brainz.workflow.form.constants.FormConstants
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class FormService(private val providerForm: ProviderForm) {

    fun findFormList(search: String): List<FormDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val responseBody = providerForm.wfGetFormListToJson(params)
        val mapper = ObjectMapper()
        val list: List<Map<String, Any>> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, Map::class.java))
        val formList = mutableListOf<FormDto>()
        for (item in list) {
            formList.add(makeFormDto(item))
        }
        return formList
    }

    fun makeFormDto(item: Map<*, *>): FormDto {
        val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val formDto = FormDto(
                formId = item["formId"] as String,
                formName = item["formName"] as String,
                formStatus = item["formStatus"] as String,
                formDesc = item["formDesc"] as String,
                createDt = ProviderUtilities().toTimezone(item["createDt"].toString(), dateTimeFormatter),
                createUserkey = item["createUserkey"] as String,
                updateDt = item["updateDt"]?.let { ProviderUtilities().toTimezone(it.toString(), dateTimeFormatter) },
                updateUserkey = item["updateUserkey"]?.toString(),
                userName = item["userName"] as String
        )
        when (item["formStatus"] as String) {
            FormConstants.FormStatus.EDIT.value, FormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
        }

        return formDto
    }

    fun getForm(formId: String): FormDto {
        val responseBody = providerForm.wfGetFormToJson(formId)
        val mapper = ObjectMapper()
        val item: Map<*, *> = mapper.readValue(responseBody, Map::class.java)
        return makeFormDto(item)
    }

    fun deleteForm(formId: String) {
        providerForm.wfDeleteForm(formId)
    }

    fun insertForm(formDto2: FormDto) {
        val userDto: AliceUserEntity = SecurityContextHolder.getContext().authentication.details as AliceUserEntity
        val formDto = FormDto(
                formId = UUID.randomUUID().toString(),
                formName = "TEST111",
                formDesc = "TEST",
                formStatus = "form.status.edit",
                formEnabled = true,
                createDt = ProviderUtilities().toGMT(LocalDateTime.now()),
                createUserkey = userDto.userKey
        )
        formDto.createDt = ProviderUtilities().toGMT(formDto.createDt)
        formDto.updateDt = formDto.updateDt?.let { ProviderUtilities().toGMT(it) }
        providerForm.wfPostForm(formDto)
    }

    fun updateForm(formDto: FormDto) {
        formDto.createDt = ProviderUtilities().toGMT(formDto.createDt)
        formDto.updateDt = formDto.updateDt?.let { ProviderUtilities().toGMT(it) }
        providerForm.wfPutForm(formDto)
    }

}
