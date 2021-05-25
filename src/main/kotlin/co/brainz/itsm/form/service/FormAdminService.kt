/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.FormComponentDto
import co.brainz.workflow.provider.dto.FormGroupDto
import co.brainz.workflow.provider.dto.FormRowDto
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
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
    fun findForms(params: LinkedHashMap<String, Any>): List<RestTemplateFormDto> {
        return wfFormService.forms(params)
    }

    /**
     * [formId]를 받아서 문서양식 마스터 데이터 조회.
     */
    fun getFormAdmin(formId: String): RestTemplateFormDto {
        return wfFormService.form(formId)
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
     * @param formData 받아서 문서양식 세부정보 반환
     */
    private fun makeFormComponentListDto(formData: String): RestTemplateFormDataDto {
        val map = mapper.readValue(formData, LinkedHashMap::class.java)
        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)

        // Form display option
        var formDisplay: LinkedHashMap<String, Any> = linkedMapOf()
        map["display"]?.let {
            formDisplay = mapper.convertValue(map["display"], linkedMapType)
        }

        // Group list in form
        val formGroupList: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
            map["group"],
            TypeFactory.defaultInstance().constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
        )
        val groupList: MutableList<FormGroupDto> = mutableListOf()
        for (formGroup in formGroupList) {
            // Group display
            var groupDisplay: LinkedHashMap<String, Any> = linkedMapOf()
            formGroup["display"]?.let {
                groupDisplay = mapper.convertValue(formGroup["display"], linkedMapType)
            }
            // Group label
            var groupLabel: LinkedHashMap<String, Any> = linkedMapOf()
            formGroup["label"]?.let {
                groupLabel = mapper.convertValue(formGroup["label"], linkedMapType)
            }
            // Row list in group
            val rowListInGroup: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
                formGroup["row"],
                TypeFactory.defaultInstance()
                    .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
            )
            val formRowList: MutableList<FormRowDto> = mutableListOf()
            for (rowInGroup in rowListInGroup) {
                // Row display
                var rowDisplay: LinkedHashMap<String, Any> = linkedMapOf()
                rowInGroup["display"]?.let {
                    rowDisplay = mapper.convertValue(rowInGroup["display"], linkedMapType)
                }
                // Component list in rowInGroup
                val rowComponentList: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
                    rowInGroup["component"],
                    TypeFactory.defaultInstance()
                        .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
                )

                val formComponentList: MutableList<FormComponentDto> = mutableListOf()
                for (component in rowComponentList) {
                    // Display in component
                    var componentDisplay: LinkedHashMap<String, Any> = linkedMapOf()
                    component["display"]?.let {
                        componentDisplay = mapper.convertValue(component["display"], linkedMapType)
                    }
                    // Label in component
                    var componentLabel: LinkedHashMap<String, Any> = linkedMapOf()
                    component["label"]?.let {
                        componentLabel = mapper.convertValue(component["label"], linkedMapType)
                    }
                    // Validate in component
                    var componentValidate: LinkedHashMap<String, Any> = linkedMapOf()
                    component["validate"]?.let {
                        componentValidate =
                            mapper.convertValue(component["validate"], linkedMapType)
                    }
                    // element in component
                    var componentElement: LinkedHashMap<String, Any> = linkedMapOf()
                    component["element"]?.let {
                        componentElement =
                            mapper.convertValue(component["element"], linkedMapType)
                    }

                    formComponentList.add(
                        FormComponentDto(
                            id = component["id"] as String,
                            type = component["type"] as String,
                            isTopic = component["isTopic"] as Boolean,
                            tags = component["tags"] as List<String>,
                            value = null,
                            display = componentDisplay,
                            label = componentLabel,
                            validate = componentValidate,
                            element = componentElement
                        )
                    )
                }

                formRowList.add(
                    FormRowDto(
                        id = rowInGroup["id"] as String,
                        display = rowDisplay,
                        component = formComponentList
                    )
                )
            }

            groupList.add(
                FormGroupDto(
                    id = formGroup["id"] as String,
                    display = groupDisplay,
                    label = groupLabel,
                    row = formRowList
                )
            )
        }

        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto

        return RestTemplateFormDataDto(
            id = (map["id"] ?: "") as String,
            name = map["name"] as String,
            desc = map["desc"] as String,
            status = (map["status"] ?: "") as String,
            category = map["category"] as String,
            display = map["display"] as LinkedHashMap<String, Any>,
            createDt = LocalDateTime.now(),
            createUserKey = aliceUserDto.userKey,
            updateDt = null,
            updateUserKey = null,
            group = groupList
        )
    }
}
