package co.brainz.itsm.form.service

import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.dto.FormDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class FormService(private val restTemplate: RestTemplate) {

    fun getFormList(): List<FormDto> {

        val responseBody = restTemplate.getForObject("https://192.168.10.50/rest/wf/forms", String::class.java)
        val mapper = ObjectMapper()
        val list: List<Map<String, Any>> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, Map::class.java))
        val formList = mutableListOf<FormDto>()
        val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        for (item in list) {
            val formDto = FormDto(
                    formId = item["formId"] as String,
                    formName = item["formName"] as String,
                    formStatus = item["formStatus"] as String,
                    formDesc = item["formDesc"] as String,
                    createDt = LocalDateTime.parse(item["createDt"].toString(), dateTimeFormatter),
                    createUserKey = item["createUserKey"] as String,
                    updateDt = item["updateDt"]?.let { LocalDateTime.parse(it.toString(), dateTimeFormatter) },
                    updateUserKey = item["updateUserKey"]?.toString()
            )
            when (item["formStatus"] as String) {
                FormConstants.FormStatus.EDIT.value, FormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
            }
            formList.add(formDto)
        }

        return formList
    }

}
