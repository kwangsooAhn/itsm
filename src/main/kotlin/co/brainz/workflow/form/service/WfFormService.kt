/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.form.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.entity.AliceTagEntity
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.workflow.component.entity.WfComponentDataEntity
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.entity.WfComponentPropertyEntity
import co.brainz.workflow.component.repository.WfComponentDataRepository
import co.brainz.workflow.component.repository.WfComponentPropertyRepository
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.mapper.WfFormMapper
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.formGroup.entity.WfFormGroupEntity
import co.brainz.workflow.formGroup.entity.WfFormGroupPropertyEntity
import co.brainz.workflow.formGroup.repository.WfFormGroupPropertyRepository
import co.brainz.workflow.formGroup.repository.WfFormGroupRepository
import co.brainz.workflow.formRow.entity.WfFormRowEntity
import co.brainz.workflow.formRow.repository.WfFormRowRepository
import co.brainz.workflow.provider.dto.ComponentDetail
import co.brainz.workflow.provider.dto.FormComponentDto
import co.brainz.workflow.provider.dto.FormGroupDto
import co.brainz.workflow.provider.dto.FormRowDto
import co.brainz.workflow.provider.dto.RestTemplateFormComponentListDto
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonParser
import java.util.UUID
import javax.transaction.Transactional
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class WfFormService(
    private val wfFormRepository: WfFormRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfComponentDataRepository: WfComponentDataRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val aliceTagRepository: AliceTagRepository,
    private val wfFormGroupRepository: WfFormGroupRepository,
    private val wfFormRowRepository: WfFormRowRepository,
    private val wfComponentPropertyRepository: WfComponentPropertyRepository,
    private val wfFormGroupPropertyRepository: WfFormGroupPropertyRepository,
    private val aliceTagService: AliceTagService
) {

    private val wfFormMapper: WfFormMapper = Mappers.getMapper(
        WfFormMapper::class.java
    )

    private val objMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Search Forms.
     *
     * @param parameters
     * @return List<RestTemplateFormDto>
     */
    fun forms(parameters: LinkedHashMap<String, Any>): List<RestTemplateFormDto> {
        var search = ""
        var status = listOf<String>()
        var offset: Long? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["status"] != null) status = parameters["status"].toString().split(",")
        if (parameters["offset"] != null) {
            offset = parameters["offset"].toString().toLong()
        }
        val queryResult = wfFormRepository.findFormEntityList(search, status, offset)
        val formList = mutableListOf<RestTemplateFormDto>()
        for (form in queryResult.results) {
            val restTemplateDto = wfFormMapper.toFormViewDto(form)
            when (restTemplateDto.status) {
                WfFormConstants.FormStatus.EDIT.value,
                WfFormConstants.FormStatus.PUBLISH.value -> restTemplateDto.editable = true
            }
            restTemplateDto.totalCount = queryResult.total
            formList.add(restTemplateDto)
        }

        return formList
    }

    /**
     * Form Info.
     *
     * @param formId
     * @return RestTemplateFormDto
     */
    fun form(formId: String): RestTemplateFormDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId).get()
        val restTemplateFormDto = wfFormMapper.toFormViewDto(formEntity)
        when (restTemplateFormDto.status) {
            WfFormConstants.FormStatus.EDIT.value,
            WfFormConstants.FormStatus.PUBLISH.value -> restTemplateFormDto.editable = true
            WfFormConstants.FormStatus.USE.value -> {
                restTemplateFormDto.editable = !wfFormRepository.findFormDocumentExist(formId)
            }
        }
        return restTemplateFormDto
    }

    /**
     * 폼을 기준으로 컴포넌트 상세 리스트를 반환.
     * 폼 편집 및 신청서, 처리할 문서 등에서 모두 사용.
     *
     * @param formId
     * @return FormComponentDto
     */
    fun getFormComponentList(formId: String): RestTemplateFormComponentListDto {
        val components: MutableList<ComponentDetail> = mutableListOf()
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId)
        val componentEntityList = wfComponentRepository.findByFormId(formId)
        for (componentEntity in componentEntityList) {
            val dataAttribute = LinkedHashMap<String, Any>()
            dataAttribute["displayType"] = ""
            dataAttribute["mappingId"] = componentEntity.mappingId
            dataAttribute["isTopic"] = componentEntity.isTopic
            dataAttribute["tag"] =
                aliceTagRepository.findByTargetId(AliceTagConstants.TagType.COMPONENT.code, componentEntity.componentId)

            val component = ComponentDetail(
                componentId = componentEntity.componentId,
                type = componentEntity.componentType,
                value = null,
                dataAttribute = dataAttribute,
                label = null,
                option = null,
                validate = null,
                header = null,
                drTableColumns = null
            )
            val componentDataEntityList = wfComponentDataRepository.findByComponentId(componentEntity.componentId)

            for (componentDataEntity in componentDataEntityList) {
                val jsonElement = JsonParser().parse(componentDataEntity.attributeValue)
                val attributeValue = LinkedHashMap<String, Any>()

                when (jsonElement.isJsonArray) {
                    true -> attributeValue["value"] = objMapper.readValue(
                        componentDataEntity.attributeValue,
                        objMapper.typeFactory.constructCollectionType(List::class.java, LinkedHashMap::class.java)
                    )
                    false -> attributeValue["value"] =
                        objMapper.readValue(componentDataEntity.attributeValue, LinkedHashMap::class.java)
                }

                val linkedMapType = TypeFactory.defaultInstance()
                    .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)
                when (componentDataEntity.attributeId) {
                    "display" -> component.display = objMapper.convertValue(attributeValue["value"], linkedMapType)
                    "label" -> component.label = objMapper.convertValue(attributeValue["value"], linkedMapType)
                    "validate" -> component.validate = objMapper.convertValue(attributeValue["value"], linkedMapType)
                    "option" -> component.option = objMapper.convertValue(
                        attributeValue["value"],
                        TypeFactory.defaultInstance().constructCollectionType(
                            MutableList::class.java,
                            LinkedHashMap::class.java
                        )
                    )
                    "header" -> component.header = objMapper.convertValue(attributeValue["value"], linkedMapType)
                    "drTableColumns" -> component.drTableColumns = objMapper.convertValue(
                        attributeValue["value"],
                        TypeFactory.defaultInstance().constructCollectionType(
                            MutableList::class.java,
                            LinkedHashMap::class.java
                        )
                    )
                }
            }
            components.add(component)
        }

        return RestTemplateFormComponentListDto(
            formId = formId,
            name = formEntity.get().formName,
            status = formEntity.get().formStatus,
            desc = formEntity.get().formDesc,
            updateDt = formEntity.get().updateDt,
            updateUserKey = formEntity.get().updateUser?.userKey,
            createDt = formEntity.get().createDt,
            createUserKey = formEntity.get().createUser?.userKey,
            components = components
        )
    }

    fun getFormComponentListFormRefactoring(formId: String): RestTemplateFormDataDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId)
        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)

        val formGroupList: MutableList<FormGroupDto> = mutableListOf()
        val formGroupEntityList = wfFormGroupRepository.findByFormId(formId)
        for (formGroup in formGroupEntityList) {

            val rowListInGroup: MutableList<FormRowDto> = mutableListOf()
            val formRowEntityList = wfFormRowRepository.findByGroupId(formGroup.formGroupId)
            for (formRow in formRowEntityList) {

                // 컴포넌트 구성
                val componentDtoList: MutableList<FormComponentDto> = mutableListOf()
                val componentEntityList = wfComponentRepository.findByRowId(formRow.formRowId)
                for (componentEntity in componentEntityList) {
                    // 기본 component dto
                    val componentDto = FormComponentDto(
                        id = componentEntity.componentId,
                        type = componentEntity.componentType,
                        isTopic = componentEntity.isTopic,
                        mapId = componentEntity.mappingId,
                        tags = aliceTagService.getTagValuesByTargetId(
                            AliceTagConstants.TagType.COMPONENT.code,
                            componentEntity.componentId
                        )
                    )

                    // component 속성 채우기
                    val componentPropertyEntityList =
                        wfComponentPropertyRepository.findByComponentId(componentEntity.componentId)
                    for (componentPropertyEntity in componentPropertyEntityList) {
                        val optionValue =
                            objMapper.readValue(componentPropertyEntity.propertyOptions, LinkedHashMap::class.java)
                        when (componentPropertyEntity.propertyType) {
                            WfFormConstants.PropertyType.DISPLAY.value -> componentDto.display =
                                objMapper.convertValue(optionValue, linkedMapType)
                            WfFormConstants.PropertyType.LABEL.value -> componentDto.label =
                                objMapper.convertValue(optionValue, linkedMapType)
                            WfFormConstants.PropertyType.VALIDATION.value -> componentDto.validation =
                                objMapper.convertValue(optionValue, linkedMapType)
                            WfFormConstants.PropertyType.ELEMENT.value -> componentDto.element =
                                objMapper.convertValue(optionValue, linkedMapType)
                        }
                    }
                    componentDtoList.add(componentDto)
                }

                val optionValue = objMapper.readValue(formRow.rowDisplayOption, LinkedHashMap::class.java)
                rowListInGroup.add(
                    FormRowDto(
                        id = formRow.formRowId,
                        display = objMapper.convertValue(optionValue, linkedMapType),
                        component = componentDtoList
                    )
                )
            }

            // 그룹 구성
            val groupDto = FormGroupDto(
                id = formGroup.formGroupId,
                row = rowListInGroup
            )

            // 그룹 속성 채우기
            val groupPropertyEntityList = wfFormGroupPropertyRepository.findByFormGroupId(formGroup.formGroupId)
            for (groupPropertyEntity in groupPropertyEntityList) {
                val optionValue = objMapper.readValue(groupPropertyEntity.propertyOptions, LinkedHashMap::class.java)
                when (groupPropertyEntity.propertyType) {
                    WfFormConstants.PropertyType.DISPLAY.value -> groupDto.display =
                        objMapper.convertValue(optionValue, linkedMapType)
                    WfFormConstants.PropertyType.LABEL.value -> groupDto.label =
                        objMapper.convertValue(optionValue, linkedMapType)
                }
            }
            formGroupList.add(groupDto)
        }

        var displayOption: LinkedHashMap<String, Any> = LinkedHashMap()
        formEntity.get().formDisplayOption?.let {
            val displayOptionMap = objMapper.readValue(it, LinkedHashMap::class.java)
            displayOption = objMapper.convertValue(displayOptionMap, linkedMapType)
        }

        return RestTemplateFormDataDto(
            id = formId,
            name = formEntity.get().formName,
            status = formEntity.get().formStatus,
            desc = formEntity.get().formDesc,
            category = formEntity.get().formCategory ?: "",
            updateDt = formEntity.get().updateDt,
            updateUserKey = formEntity.get().updateUser?.userKey,
            createDt = formEntity.get().createDt,
            createUserKey = formEntity.get().createUser?.userKey,
            group = formGroupList,
            displayOption = displayOption
        )
    }

    /**
     * Get component data.
     */
    private fun getComponentData(
        resultComponentEntity: WfComponentEntity,
        component: ComponentDetail
    ): MutableList<WfComponentDataEntity> {
        val wfComponentDataEntities: MutableList<WfComponentDataEntity> = mutableListOf()

        // wf_comp_data 저장
        var componentDataEntity = WfComponentDataEntity(
            componentId = resultComponentEntity.componentId,
            attributeId = "display",
            attributeValue = objMapper.writeValueAsString(component.display),
            attributes = resultComponentEntity
        )
        wfComponentDataEntities.add(componentDataEntity)

        component.label?.let {
            if (it.size > 0) {
                componentDataEntity = WfComponentDataEntity(
                    componentId = resultComponentEntity.componentId,
                    attributeId = "label",
                    attributeValue = objMapper.writeValueAsString(it),
                    attributes = resultComponentEntity
                )
                wfComponentDataEntities.add(componentDataEntity)
            }
        }

        component.validate?.let {
            if (it.size > 0) {
                componentDataEntity = WfComponentDataEntity(
                    componentId = resultComponentEntity.componentId,
                    attributeId = "validate",
                    attributeValue = objMapper.writeValueAsString(it),
                    attributes = resultComponentEntity
                )
                wfComponentDataEntities.add(componentDataEntity)
            }
        }

        component.option?.let {
            if (it.size > 0) {
                componentDataEntity = WfComponentDataEntity(
                    componentId = resultComponentEntity.componentId,
                    attributeId = "option",
                    attributeValue = objMapper.writeValueAsString(it),
                    attributes = resultComponentEntity
                )
                wfComponentDataEntities.add(componentDataEntity)
            }
        }

        component.header?.let {
            if (it.size > 0) {
                componentDataEntity = WfComponentDataEntity(
                    componentId = resultComponentEntity.componentId,
                    attributeId = "header",
                    attributeValue = objMapper.writeValueAsString(it),
                    attributes = resultComponentEntity
                )
                wfComponentDataEntities.add(componentDataEntity)
            }
        }

        component.drTableColumns?.let {
            if (it.size > 0) {
                componentDataEntity = WfComponentDataEntity(
                    componentId = resultComponentEntity.componentId,
                    attributeId = "drTableColumns",
                    attributeValue = objMapper.writeValueAsString(it),
                    attributes = resultComponentEntity
                )
                wfComponentDataEntities.add(componentDataEntity)
            }
        }

        return wfComponentDataEntities
    }

    private fun getComponentDataFormRefactoring(
        resultComponentEntity: WfComponentEntity,
        component: FormComponentDto
    ): MutableList<WfComponentPropertyEntity> {
        val wfComponentPropertyEntities: MutableList<WfComponentPropertyEntity> = mutableListOf()

        // wf_comp_data 저장
        var componentPropertyEntity = WfComponentPropertyEntity(
            componentId = resultComponentEntity.componentId,
            propertyType = WfFormConstants.PropertyType.DISPLAY.value,
            propertyOptions = objMapper.writeValueAsString(component.display),
            properties = resultComponentEntity
        )
        wfComponentPropertyEntities.add(componentPropertyEntity)

        component.validation?.let {
            if (it.size > 0) {
                componentPropertyEntity = WfComponentPropertyEntity(
                    componentId = resultComponentEntity.componentId,
                    propertyType = WfFormConstants.PropertyType.VALIDATION.value,
                    propertyOptions = objMapper.writeValueAsString(it),
                    properties = resultComponentEntity
                )
                wfComponentPropertyEntities.add(componentPropertyEntity)
            }
        }

        component.element?.let {
            if (it.size > 0) {
                componentPropertyEntity = WfComponentPropertyEntity(
                    componentId = resultComponentEntity.componentId,
                    propertyType = WfFormConstants.PropertyType.ELEMENT.value,
                    propertyOptions = objMapper.writeValueAsString(it),
                    properties = resultComponentEntity
                )
                wfComponentPropertyEntities.add(componentPropertyEntity)
            }
        }

        if (component.label.size > 0) {
            componentPropertyEntity = WfComponentPropertyEntity(
                componentId = resultComponentEntity.componentId,
                propertyType = WfFormConstants.PropertyType.LABEL.value,
                propertyOptions = objMapper.writeValueAsString(component.label),
                properties = resultComponentEntity
            )
            wfComponentPropertyEntities.add(componentPropertyEntity)
        }

        return wfComponentPropertyEntities
    }

    /**
     * Create Form.
     *
     * @param restTemplateFormDto
     * @return RestTemplateFormDto
     */
    fun createForm(restTemplateFormDto: RestTemplateFormDto): RestTemplateFormDto {
        val formEntity = WfFormEntity(
            formId = restTemplateFormDto.id,
            formName = restTemplateFormDto.name,
            formDesc = restTemplateFormDto.desc,
            formStatus = restTemplateFormDto.status,
            createDt = restTemplateFormDto.createDt,
            createUser = restTemplateFormDto.createUserKey?.let { aliceUserRepository.findAliceUserEntityByUserKey(it) }
        )
        val dataEntity = wfFormRepository.save(formEntity)

        return RestTemplateFormDto(
            id = dataEntity.formId,
            name = dataEntity.formName,
            status = dataEntity.formStatus,
            desc = dataEntity.formDesc,
            editable = true,
            createUserKey = dataEntity.createUser?.userKey,
            createDt = dataEntity.createDt
        )
    }

    /**
     * Delete Form.
     *
     * @param formId
     */
    @Transactional
    fun deleteForm(formId: String): Boolean {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId).get()
        val documentEntity = wfDocumentRepository.findByForm(formEntity)
        if (documentEntity == null) {
            wfFormRepository.removeWfFormEntityByFormId(formId)
            return true
        }
        return false
    }

    /**
     * Update Form.
     *
     * @param restTemplateFormDto
     * @return Boolean
     */
    fun updateForm(restTemplateFormDto: RestTemplateFormDto): Boolean {
        this.updateFormEntity(restTemplateFormDto)
        return true
    }

    /**
     * Insert, Update Form Data.
     *
     * @param formData
     */
    @Transactional
    fun saveFormData(formData: RestTemplateFormDataDto): Boolean {

        // Delete
        val groupEntities = formData.id?.let { wfFormGroupRepository.findByFormId(it) }
        groupEntities?.let {
            for (group in groupEntities) {
                val rowEntities = wfFormRowRepository.findByGroupId(group.formGroupId)
                rowEntities.let {
                    for (row in rowEntities) {
                        val componentEntities = wfComponentRepository.findByRowId(row.formRowId)
                        for (component in componentEntities) {
                            wfComponentRepository.deleteComponentEntityByComponentId(component.componentId)
                            aliceTagRepository.deleteByTargetId(
                                AliceTagConstants.TagType.COMPONENT.code,
                                component.componentId
                            )
                        }
                        wfFormRowRepository.delete(row)
                    }
                }
                wfFormGroupRepository.delete(group)
            }
        }

        // Update Form
        val resultFormEntity =
            this.updateFormEntity(wfFormMapper.toRestTemplateFormDto(formData))

        // Insert component, attribute
        for (group in formData.group.orEmpty()) {
            val wfFormGroupPropertyEntities: MutableList<WfFormGroupPropertyEntity> = mutableListOf()
            val currentGroup = wfFormGroupRepository.save(
                WfFormGroupEntity(
                    formGroupId = group.id,
                    form = resultFormEntity
                )
            )

            wfFormGroupPropertyEntities.addAll(this.getFormGroupPropertyFormRefactoring(currentGroup, group))

            if (wfFormGroupPropertyEntities.isNotEmpty()) {
                wfFormGroupPropertyRepository.saveAll(wfFormGroupPropertyEntities)
            }

            group.row?.let {
                for (row in it) {
                    val currentRow = wfFormRowRepository.save(
                        WfFormRowEntity(
                            formRowId = row.id,
                            formGroup = currentGroup,
                            rowDisplayOption = objMapper.writeValueAsString(row.display)
                        )
                    )
                    row.component.let { components ->
                        val wfComponentPropertyEntities: MutableList<WfComponentPropertyEntity> = mutableListOf()
                        for (component in components) {
                            val resultComponentEntity =
                                this.saveComponentFormRefactoring(currentRow, resultFormEntity, component)
                            wfComponentPropertyEntities.addAll(
                                this.getComponentDataFormRefactoring(
                                    resultComponentEntity,
                                    component
                                )
                            )
                        }
                        if (wfComponentPropertyEntities.isNotEmpty()) {
                            wfComponentPropertyRepository.saveAll(wfComponentPropertyEntities)
                        }
                    }
                }
            }
        }

        return true
    }

    /**
     * Save as Form.
     *
     * @param restTemplateFormDataDto
     * @return RestTemplateFormDto
     */
    fun saveAsFormData(restTemplateFormDataDto: RestTemplateFormDataDto): RestTemplateFormDto {
        val formDataDto = RestTemplateFormDto(
            name = restTemplateFormDataDto.name,
            status = restTemplateFormDataDto.status,
            desc = restTemplateFormDataDto.desc,
            editable = true,
            createUserKey = restTemplateFormDataDto.createUserKey,
            createDt = restTemplateFormDataDto.createDt
        )
        val wfFormDto = createForm(formDataDto)
        restTemplateFormDataDto.id = wfFormDto.id
        when (restTemplateFormDataDto.status) {
            WfFormConstants.FormStatus.PUBLISH.value, WfFormConstants.FormStatus.DESTROY.value -> wfFormDto.editable =
                false
        }

        for (group in restTemplateFormDataDto.group.orEmpty()) {
            group.id = UUID.randomUUID().toString().replace("-", "")
            for (row in group.row.orEmpty()) {
                row.id = UUID.randomUUID().toString().replace("-", "")
                for (component in row.component) {
                    component.id = UUID.randomUUID().toString().replace("-", "")
                }
            }
        }
        saveFormData(restTemplateFormDataDto)

        return wfFormDto
    }

    /**
     * Update form entity.
     */
    private fun updateFormEntity(restTemplateFormDto: RestTemplateFormDto): WfFormEntity {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(restTemplateFormDto.id)
        formEntity.get().formName = restTemplateFormDto.name
        formEntity.get().formDesc = restTemplateFormDto.desc
        formEntity.get().formStatus = restTemplateFormDto.status
        formEntity.get().formDisplayOption = objMapper.writeValueAsString(restTemplateFormDto.displayOption)
        formEntity.get().formCategory = restTemplateFormDto.category
        formEntity.get().updateDt = restTemplateFormDto.updateDt
        formEntity.get().updateUser = restTemplateFormDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        return wfFormRepository.save(formEntity.get())
    }

    /**
     * Save component.
     */
    private fun saveComponent(resultFormEntity: WfFormEntity, component: ComponentDetail): WfComponentEntity {
        var mappingId = ""
        var isTopic = false
        val dataAttribute: java.util.LinkedHashMap<*, *>? =
            objMapper.convertValue(component.dataAttribute, LinkedHashMap::class.java)
        if (dataAttribute != null) {
            if (dataAttribute.containsKey("mappingId")) {
                mappingId = dataAttribute["mappingId"].toString().trim()
            }
            if (dataAttribute.containsKey("isTopic")) {
                isTopic = dataAttribute["isTopic"] as Boolean
            }
            if (dataAttribute.containsKey("tag")) {
                val tagList = dataAttribute["tag"] as ArrayList<LinkedHashMap<String, String>>
                tagList.forEach { tag ->
                    tag["value"]?.let {
                        aliceTagRepository.save(
                            AliceTagEntity(
                                tagType = AliceTagConstants.TagType.COMPONENT.code,
                                tagValue = it,
                                targetId = component.componentId
                            )
                        )
                    }
                }
            }
        }
        val componentEntity = WfComponentEntity(
            componentId = component.componentId,
            componentType = component.type,
            mappingId = mappingId,
            isTopic = isTopic,
            form = resultFormEntity
        )

        return wfComponentRepository.save(componentEntity)
    }

    private fun saveComponentFormRefactoring(
        currentRowEntity: WfFormRowEntity,
        currentFormEntity: WfFormEntity,
        component: FormComponentDto
    ): WfComponentEntity {
        val tagList = component.tags as List<String>
        tagList.forEach { tagValue ->
            aliceTagRepository.save(
                AliceTagEntity(
                    tagType = AliceTagConstants.TagType.COMPONENT.code,
                    tagValue = tagValue,
                    targetId = component.id
                )
            )
        }

        val componentEntity = WfComponentEntity(
            componentId = component.id,
            componentType = component.type,
            mappingId = component.mapId,
            isTopic = component.isTopic,
            form = currentFormEntity, // 리팩토링 후 삭제 대상 필드
            formRow = currentRowEntity
        )

        return wfComponentRepository.save(componentEntity)
    }

    private fun getFormGroupPropertyFormRefactoring(
        formGroupEntity: WfFormGroupEntity,
        group: FormGroupDto
    ): MutableList<WfFormGroupPropertyEntity> {
        val wfFormGroupPropertyEntities: MutableList<WfFormGroupPropertyEntity> = mutableListOf()

        var formGroupPropertyEntity = WfFormGroupPropertyEntity(
            formGroupId = formGroupEntity.formGroupId,
            propertyType = "display",
            propertyOptions = objMapper.writeValueAsString(group.display),
            properties = formGroupEntity
        )
        wfFormGroupPropertyEntities.add(formGroupPropertyEntity)

        group.label?.let {
            if (it.size > 0) {
                formGroupPropertyEntity = WfFormGroupPropertyEntity(
                    formGroupId = formGroupEntity.formGroupId,
                    propertyType = "label",
                    propertyOptions = objMapper.writeValueAsString(it),
                    properties = formGroupEntity
                )
                wfFormGroupPropertyEntities.add(formGroupPropertyEntity)
            }
        }
        return wfFormGroupPropertyEntities
    }
}
