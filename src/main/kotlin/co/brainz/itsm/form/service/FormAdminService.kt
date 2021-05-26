/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.ComponentDetail
import co.brainz.workflow.provider.dto.RestTemplateFormComponentListDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateFormListReturnDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class FormAdminService(
    private val wfFormService: WfFormService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 문서양식 데이터 목록 조회.
     */
    fun findForms(params: LinkedHashMap<String, Any>): RestTemplateFormListReturnDto {
        return wfFormService.getforms(params)
    }

    /**
     * [formId]를 받아서 문서양식 마스터 데이터 조회.
     */
    fun getFormAdmin(formId: String): RestTemplateFormDto {
        return wfFormService.getformDetail(formId)
    }

    /**
     * [formId], [restTemplateFormDto]를 받아서 프로세스 마스터 데이터 업데이트.
     */
    fun updateFrom(formId: String, restTemplateFormDto: RestTemplateFormDto): Boolean {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateFormDto.updateDt = LocalDateTime.now()
        restTemplateFormDto.updateUserKey = userDetails.userKey
        return wfFormService.updateForm(restTemplateFormDto)
    }

    /**
     * [formData] 받아서 새이름으로 저장하기.
     */
    fun saveAsForm(formData: String): String {
        val formComponentListDto = makeFormComponentListDto(formData)
        formComponentListDto.status = RestTemplateConstants.FormStatus.EDIT.value
        return wfFormService.saveAsFormData(formComponentListDto).id
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
        return wfFormService.createForm(restTemplateFormDto).id
    }

    /**
     * [formData] 받아서 문서양식 세부정보 반환
     */
    private fun makeFormComponentListDto(formData: String): RestTemplateFormComponentListDto {
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
            if (dataAttribute["tag"] is LinkedHashMap<*, *>) {
                val dummyTag: ArrayList<LinkedHashMap<String, Any>> = mutableListOf<LinkedHashMap<String, Any>>() as ArrayList<LinkedHashMap<String, Any>>
                dummyTag.add(dataAttribute["tag"] as java.util.LinkedHashMap<String, Any>)
                dataAttribute["tag"] = dummyTag
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

            var header: LinkedHashMap<String, Any> = linkedMapOf()
            component["header"]?.let {
                header = mapper.convertValue(component["header"], linkedMapType)
            }

            var field: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
            component["field"]?.let {
                field = mapper.convertValue(
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
                    option = option,
                    header = header,
                    drTableColumns = field
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
