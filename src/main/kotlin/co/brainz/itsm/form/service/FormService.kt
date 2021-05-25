/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.dto.ComponentDetail
import co.brainz.workflow.provider.dto.FormComponentDto
import co.brainz.workflow.provider.dto.FormGroupDto
import co.brainz.workflow.provider.dto.FormRowDto
import co.brainz.workflow.provider.dto.RestTemplateFormComponentListDto
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class FormService(
    private val wfFormService: WfFormService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getFormData(formId: String): RestTemplateFormComponentListDto {
        return wfFormService.getFormComponentList(formId)
    }

    fun getFormDataFormRefactoring(formId: String): RestTemplateFormDataDto {
        return wfFormService.getFormComponentListFormRefactoring(formId)
    }

    /**
     * [formId], 를 받아서 문서양식을 삭제한다.
     */
    fun deleteForm(formId: String): Boolean {
        return wfFormService.deleteForm(formId)
    }

    /**
     * [formId], [formData]를 받아서 문서양식을 저장한다.
     */
    fun saveFormData(formId: String, formData: String): Boolean {
        val formComponentListDto = makeFormComponentListDto(formData)
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        formComponentListDto.updateDt = LocalDateTime.now()
        formComponentListDto.updateUserKey = aliceUserDto.userKey
        return wfFormService.saveFormData(formComponentListDto)
    }

    /**
     * [formId], [formData]를 받아서 문서양식을 저장한다.
     */
    fun saveFormDataFormRefactoring(formId: String, formData: String): Boolean {
        val formComponentListDto = makeFormComponentListDtoFromRefactoring(formData)
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        formComponentListDto.updateDt = LocalDateTime.now()
        formComponentListDto.updateUserKey = aliceUserDto.userKey
        return wfFormService.saveFormDataFromRefactoring(formComponentListDto)
    }

    /**
     * [formData]를 받아서 문서양식을 반환한다.
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

            var header: LinkedHashMap<String, Any> = linkedMapOf()
            component["header"]?.let {
                header = mapper.convertValue(component["header"], linkedMapType)
            }

            var drTableColumns: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
            component["drTableColumns"]?.let {
                drTableColumns = mapper.convertValue(
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
                    drTableColumns = drTableColumns
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

    /**
     * [formData]를 받아서 문서양식을 반환한다.
     */
    private fun makeFormComponentListDtoFromRefactoring(formData: String): RestTemplateFormDataDto {
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
