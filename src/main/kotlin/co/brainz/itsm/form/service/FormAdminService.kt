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
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class FormAdminService(private val restTemplate: RestTemplateProvider) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 문서양식 데이터 목록 조회.
     */
    fun findForms(params: LinkedMultiValueMap<String, String>): List<RestTemplateFormDto> {
        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.GET_FORMS.url, parameters = params)
        val responseBody = restTemplate.get(urlDto)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateFormDto::class.java)
        )
    }

    /**
     * [formId]를 받아서 문서양식 마스터 데이터 조회.
     */
    fun getFormAdmin(formId: String): RestTemplateFormDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.GET_FORM.url.replace(
                restTemplate.getKeyRegex(),
                formId
            )
        )
        return mapper.readValue(
            restTemplate.get(url),
            mapper.typeFactory.constructType(RestTemplateFormDto::class.java)
        )
    }

    /**
     * [formId], [restTemplateFormDto]를 받아서 프로세스 마스터 데이터 업데이트.
     */
    fun updateFrom(formId: String, restTemplateFormDto: RestTemplateFormDto): Boolean {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateFormDto.updateDt = LocalDateTime.now()
        restTemplateFormDto.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.PUT_FORM.url.replace(
                restTemplate.getKeyRegex(),
                formId
            )
        )
        val responseEntity = restTemplate.update(url, restTemplateFormDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * [formData] 받아서 새이름으로 저장하기.
     */
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

    /**
     * [restTemplateFormDto] 받아서 문서양식 저장하기.
     */
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

    /**
     * [formData] 받아서 문서양식 세부정보 반환
     */
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
                display = mapper.convertValue(component["display"], linkedMapType)
            }

            var label: LinkedHashMap<String, Any> = linkedMapOf()
            component["label"]?.let {
                label = mapper.convertValue(component["label"], linkedMapType)
            }

            var validate: LinkedHashMap<String, Any> = linkedMapOf()
            component["validate"]?.let {
                validate = mapper.convertValue(component["validate"], linkedMapType)
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
            status = (map["status"] ?: "") as String,
            createDt = LocalDateTime.now(),
            createUserKey = aliceUserDto.userKey,
            updateDt = null,
            updateUserKey = null,
            components = componentDetailList
        )
    }
}
