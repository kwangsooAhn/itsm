/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.form.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.label.constants.AliceLabelConstants
import co.brainz.framework.label.dto.AliceLabelDto
import co.brainz.framework.label.service.AliceLabelService
import co.brainz.workflow.component.entity.WfComponentDataEntity
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentDataRepository
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.mapper.WfFormMapper
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.provider.dto.ComponentDetail
import co.brainz.workflow.provider.dto.RestTemplateFormComponentListDto
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
    private val aliceLabelService: AliceLabelService
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

            // 해당 컴포넌트에 연결된 라벨 찾기
            val labelsArray = ArrayList<LinkedHashMap<String, String?>>()
            val labelEntities =
                aliceLabelService.getLabels(AliceLabelConstants.LABEL_TARGET_COMPONENT, componentEntity.componentId)

            // 저장과 마찬가지로 폼 디자이너 화면에서 사용하기 위해서 Array 형태로 변환이 필요.
            for (labelEntity in labelEntities) {
                labelsArray.add(linkedMapOf(Pair(labelEntity.labelKey, labelEntity.labelValue)))
            }

            // 폼 전송용 데이터 규격으로 변환
            val labelList = LinkedHashMap<String, Any>()
            labelList["label_target"] = AliceLabelConstants.LABEL_TARGET_COMPONENT
            labelList["target_id"] = componentEntity.componentId
            labelList["label"] = labelsArray
            dataAttribute["labelList"] = labelList

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
     * @param restTemplateFormComponentListDto
     */
    @Transactional
    fun saveFormData(restTemplateFormComponentListDto: RestTemplateFormComponentListDto): Boolean {

        // Delete component, attribute
        val componentEntities = restTemplateFormComponentListDto.formId?.let { wfComponentRepository.findByFormId(it) }
        val componentIds: MutableList<String> = mutableListOf()
        if (componentEntities != null) {
            for (component in componentEntities) {
                componentIds.add(component.componentId)
            }
            if (componentIds.isNotEmpty()) {
                wfComponentRepository.deleteComponentEntityByComponentIdIn(componentIds)
                for (componentId in componentIds) {
                    aliceLabelService.deleteLabels(
                        AliceLabelDto(
                            labelTarget = AliceLabelConstants.LABEL_TARGET_COMPONENT,
                            labelTargetId = componentId
                        )
                    )
                }
            }
        }

        // Update Form
        val resultFormEntity =
            this.updateFormEntity(wfFormMapper.toRestTemplateFormDto(restTemplateFormComponentListDto))

        // Insert component, attribute
        val wfComponentDataEntities: MutableList<WfComponentDataEntity> = mutableListOf()
        for (component in restTemplateFormComponentListDto.components) {
            // wf_component 저장
            val resultComponentEntity = this.saveComponent(resultFormEntity, component)
            // component data 가져오기
            wfComponentDataEntities.addAll(this.getComponentData(resultComponentEntity, component))
        }
        if (wfComponentDataEntities.isNotEmpty()) {
            wfComponentDataRepository.saveAll(wfComponentDataEntities)
        }
        return true
    }

    /**
     * Save as Form.
     *
     * @param restTemplateFormComponentListDto
     * @return RestTemplateFormDto
     */
    fun saveAsFormData(restTemplateFormComponentListDto: RestTemplateFormComponentListDto): RestTemplateFormDto {
        val formDataDto = RestTemplateFormDto(
            name = restTemplateFormComponentListDto.name,
            status = restTemplateFormComponentListDto.status,
            desc = restTemplateFormComponentListDto.desc,
            editable = true,
            createUserKey = restTemplateFormComponentListDto.createUserKey,
            createDt = restTemplateFormComponentListDto.createDt
        )
        val wfFormDto = createForm(formDataDto)
        restTemplateFormComponentListDto.formId = wfFormDto.id
        when (restTemplateFormComponentListDto.status) {
            WfFormConstants.FormStatus.PUBLISH.value, WfFormConstants.FormStatus.DESTROY.value -> wfFormDto.editable =
                false
        }

        for ((idx, component) in restTemplateFormComponentListDto.components.withIndex()) {
            val prevComponentId = component.componentId
            val changeComponentId = UUID.randomUUID().toString().replace("-", "")
            // 아코디언 컴포넌트
            if (component.type == "accordion-start") {
                for (i in idx until restTemplateFormComponentListDto.components.size) {
                    val item = restTemplateFormComponentListDto.components[i]
                    if (item.type == "accordion-end" && item.display["startId"] == prevComponentId) {
                        item.display["startId"] = changeComponentId

                        item.componentId = UUID.randomUUID().toString().replace("-", "")
                        component.display["endId"] = item.componentId
                    }
                }
            }
            if (component.type != "accordion-end") {
                component.componentId = changeComponentId
            }
        }
        saveFormData(restTemplateFormComponentListDto)

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
        var labelList = LinkedHashMap<String, Any>()
        val dataAttribute: java.util.LinkedHashMap<*, *>? =
            objMapper.convertValue(component.dataAttribute, LinkedHashMap::class.java)
        if (dataAttribute != null) {
            if (dataAttribute.containsKey("mappingId")) {
                mappingId = dataAttribute["mappingId"].toString().trim()
            }
            if (dataAttribute.containsKey("isTopic")) {
                isTopic = dataAttribute["isTopic"] as Boolean
            }
            if (dataAttribute.containsKey("labelList")) {
                val labels = LinkedHashMap<String, String?>()
                labelList = dataAttribute["labelList"] as LinkedHashMap<String, Any>
                labelList["label"]?.let {
                    val labelsArrayList = labelList["label"] as ArrayList<LinkedHashMap<String, String?>>

                    // 화면에서 전달된 라벨리스트가 size 가 1개인 map 의 Array 형태로 구성되었음.
                    // 아래는 forEach 가 이중으로 작성되었으나 내부의 forEach 는 1번씩만 동작.
                    // 하나씩 꺼내서 저장용 맵을 구성.

                    labelsArrayList.forEach { label ->
                        label.keys.forEach {
                            labels[it] = label[it]
                        }
                    }

                    aliceLabelService.addLabels(
                        AliceLabelDto(
                            labelTarget = labelList["label_target"] as String,
                            labelTargetId = labelList["target_id"] as String,
                            labels = labels
                        )
                    )
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
}
