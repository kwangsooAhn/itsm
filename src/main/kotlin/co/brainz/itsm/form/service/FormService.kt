package co.brainz.itsm.form.service

import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.dto.FormDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

import org.springframework.web.util.UriComponentsBuilder
import java.net.InetAddress
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class FormService(private val restTemplate: RestTemplate) {

    @Value("\${server.protocol}")
    lateinit var protocol: String

    @Value("\${server.port}")
    lateinit var port: String

    fun makeUri(callUrl: String, params: MultiValueMap<String, String>): URI {
        val formUrl = protocol + "://" + InetAddress.getLocalHost().hostAddress + ":" + port + callUrl
        val uri = UriComponentsBuilder.fromHttpUrl(formUrl)
        if (params.isNotEmpty()) {
            uri.queryParams(params)
        }

        return uri.build().toUri()
    }

    fun findFormList(search: String): List<FormDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val uri = makeUri("/rest/wf/forms", params)
        val responseBody = restTemplate.getForObject(uri, String::class.java)
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
                createDt = LocalDateTime.parse(item["createDt"].toString(), dateTimeFormatter),
                createUserKey = item["createUserKey"] as String,
                updateDt = item["updateDt"]?.let { LocalDateTime.parse(it.toString(), dateTimeFormatter) },
                updateUserKey = item["updateUserKey"]?.toString()
        )
        when (item["formStatus"] as String) {
            FormConstants.FormStatus.EDIT.value, FormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
        }

        return formDto
    }

    fun getForm(formId: String): FormDto {
        val uri = makeUri("/rest/wf/forms/$formId", LinkedMultiValueMap<String, String>())
        val responseBody = restTemplate.getForObject(uri, String::class.java)
        val mapper = ObjectMapper()
        val item: Map<*, *> = mapper.readValue(responseBody, Map::class.java)
        return makeFormDto(item)
    }

    fun deleteForm(formId: String) {
        restTemplate.delete(makeUri("/rest/wf/forms/$formId", LinkedMultiValueMap<String, String>()))
    }

    fun insertForm(formDto: FormDto) {
        val uri = makeUri("/rest/wf/forms", LinkedMultiValueMap<String, String>())

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()

        //TODO: formDto를 MultiValueMap에 저장

        val requestEntity = HttpEntity(parameters, headers)
        restTemplate.postForObject(uri, requestEntity, String::class.java)

    }

    fun updateForm(formDto: FormDto) {
        val uri = makeUri("/rest/wf/forms/${formDto.formId}", LinkedMultiValueMap<String, String>())
        restTemplate.put(uri, formDto)
    }

}
