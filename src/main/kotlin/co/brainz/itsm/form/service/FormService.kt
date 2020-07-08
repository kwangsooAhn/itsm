package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.ComponentDetail
import co.brainz.workflow.provider.dto.RestTemplateFormComponentListDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class FormService(private val restTemplate: RestTemplateProvider) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun findForms(params: LinkedMultiValueMap<String, String>): List<RestTemplateFormDto> {
        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.GET_FORMS.url, parameters = params)
        val responseBody = restTemplate.get(urlDto)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateFormDto::class.java)
        )
    }

    fun getFormData(formId: String): String {
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.GET_FORM_DATA.url.replace(
                restTemplate.getKeyRegex(),
                formId
            )
        )
        return restTemplate.get(urlDto)
    }

    fun createForm(restTemplateFormDto: RestTemplateFormDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateFormDto.status = RestTemplateConstants.FormStatus.EDIT.value
        restTemplateFormDto.createUserKey = aliceUserDto.userKey
        restTemplateFormDto.createDt = LocalDateTime.now()
        restTemplateFormDto.updateDt = LocalDateTime.now()
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.POST_FORM.url)
        val responseEntity = restTemplate.create(url, restTemplateFormDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                val dataDto = mapper.readValue(responseEntity.body.toString(), RestTemplateFormDto::class.java)
                dataDto.id
            }
            false -> ""
        }
    }

    fun deleteForm(formId: String): ResponseEntity<String> {
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.DELETE_FORM.url.replace(
                restTemplate.getKeyRegex(),
                formId
            )
        )
        return restTemplate.delete(urlDto)
    }

    fun saveFormData(formId: String, formData: String): Boolean {
        val formComponentListDto = makeFormComponentListDto(formData)
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        formComponentListDto.updateDt = LocalDateTime.now()
        formComponentListDto.updateUserKey = aliceUserDto.userKey
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.PUT_FORM_DATA.url.replace(restTemplate.getKeyRegex(), formId)
        )
        val responseEntity = restTemplate.update(urlDto, formComponentListDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    fun saveAsForm(formData: String): String {
        val formComponentListDto = makeFormComponentListDto(formData)
        formComponentListDto.status = RestTemplateConstants.FormStatus.EDIT.value

        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.POST_FORM_SAVE_AS.url)
        val responseEntity = restTemplate.createToSave(urlDto, formComponentListDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                val dataDto = mapper.readValue(responseEntity.body.toString(), RestTemplateFormDto::class.java)
                dataDto.id
            }
            false -> ""
        }
    }

    fun makeFormComponentListDto(formData: String): RestTemplateFormComponentListDto {
        val map = mapper.readValue(formData, LinkedHashMap::class.java)
        val components: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
            map["components"],
            TypeFactory.defaultInstance().constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
        )

        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)
        val componentDetailList: MutableList<ComponentDetail> = mutableListOf()
        for (component in components) {
            var dataAttribute: LinkedHashMap<String, Any> = linkedMapOf()
            component["dataAttribute"]?.let {
                dataAttribute =
                    mapper.convertValue(
                        component["dataAttribute"],
                        linkedMapType
                    )
            }

            var display: LinkedHashMap<String, Any> = linkedMapOf()
            component["display"]?.let {
                display =
                    mapper.convertValue(component["display"], linkedMapType)
            }

            var label: LinkedHashMap<String, Any> = linkedMapOf()
            component["label"]?.let {
                label = mapper.convertValue(component["label"], linkedMapType)
            }

            var validate: LinkedHashMap<String, Any> = linkedMapOf()
            component["validate"]?.let {
                validate =
                    mapper.convertValue(component["validate"], linkedMapType)
            }

            var option: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
            component["option"]?.let {
                option = mapper.convertValue(
                    it, TypeFactory.defaultInstance()
                        .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
                )
            }

            componentDetailList.add(
                ComponentDetail(
                    componentId = component["componentId"] as String,
                    type = component["type"] as String,
                    value = null,
                    dataAttribute = dataAttribute,
                    display = display,
                    label = label,
                    validate = validate,
                    option = option
                )
            )
        }

        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        return RestTemplateFormComponentListDto(
            formId = (map["formId"] ?: "") as String,
            name = map["name"] as String,
            desc = map["desc"] as String,
            status = (map["status"] ?:"") as String,
            createDt = LocalDateTime.now(),
            createUserKey = aliceUserDto.userKey,
            updateDt = null,
            updateUserKey = null,
            components = componentDetailList
        )
    }
}
