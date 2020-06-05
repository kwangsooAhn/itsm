package co.brainz.workflow.form.service

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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonParser
import java.util.Optional
import java.util.UUID
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class WfFormService(
    private val wfFormRepository: WfFormRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfComponentDataRepository: WfComponentDataRepository,
    private val wfDocumentRepository: WfDocumentRepository
) {

    private val wfFormMapper: WfFormMapper = Mappers.getMapper(
        WfFormMapper::class.java)

    /**
     * Search Forms.
     *
     * @param parameters
     * @return List<RestTemplateFormDto>
     */
    fun forms(parameters: LinkedHashMap<String, Any>): List<RestTemplateFormDto> {
        var search = ""
        var status = listOf<String>()
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["status"] != null) status = parameters["status"].toString().split(",")
        val formEntityList = wfFormRepository.findFormEntityList(search, status)
        val formList = mutableListOf<RestTemplateFormDto>()
        formEntityList.forEach { formEntity ->
            val restTemplateDto = wfFormMapper.toFormViewDto(formEntity)
            when (restTemplateDto.status) {
                WfFormConstants.FormStatus.EDIT.value,
                WfFormConstants.FormStatus.PUBLISH.value -> restTemplateDto.editable = true
            }
            formList.add(restTemplateDto)
        }

        return formList
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
            createUserKey = restTemplateFormDto.createUserKey
        )
        val dataEntity = wfFormRepository.save(formEntity)

        return RestTemplateFormDto(
            id = dataEntity.formId,
            name = dataEntity.formName,
            status = dataEntity.formStatus,
            desc = dataEntity.formDesc,
            editable = true,
            createUserKey = dataEntity.createUserKey,
            createDt = dataEntity.createDt
        )
    }

    /**
     * Delete Form.
     *
     * @param formId
     */
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
     * Form Info.
     *
     * @param formId
     * @return RestTemplateFormDto
     */
    fun form(formId: String): RestTemplateFormDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId).get()
        val restTemplateFormDto = wfFormMapper.toFormDto(formEntity)
        when (restTemplateFormDto.status) {
            WfFormConstants.FormStatus.EDIT.value,
            WfFormConstants.FormStatus.PUBLISH.value -> restTemplateFormDto.editable = true
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

            val component = ComponentDetail(
                componentId = componentEntity.componentId,
                type = componentEntity.componentType,
                values = null,
                dataAttribute = dataAttribute,
                label = null,
                option = null,
                validate = null
            )

            val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
            val componentDataEntityList = wfComponentDataRepository.findByComponentId(componentEntity.componentId)

            for (componentDataEntity in componentDataEntityList) {
                val jsonElement = JsonParser().parse(componentDataEntity.attributeValue)
                var attributeValue = LinkedHashMap<String, Any>()

                when (jsonElement.isJsonArray) {
                    true -> attributeValue["value"] = mapper.readValue(
                        componentDataEntity.attributeValue,
                        mapper.typeFactory.constructCollectionType(List::class.java, LinkedHashMap::class.java)
                    )
                    false -> attributeValue["value"] =
                        mapper.readValue(componentDataEntity.attributeValue, LinkedHashMap::class.java)
                }

                when (componentDataEntity.attributeId) {
                    "display" -> component.display = attributeValue["value"] as LinkedHashMap<String, Any>
                    "label" -> component.label = attributeValue["value"] as LinkedHashMap<String, Any>
                    "validate" -> component.validate = attributeValue["value"] as LinkedHashMap<String, Any>
                    "option" -> component.option = attributeValue["value"] as MutableList<LinkedHashMap<String, Any>>
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
            updateUserKey = formEntity.get().updateUserKey,
            createDt = formEntity.get().createDt,
            createUserKey = formEntity.get().createUserKey,
            components = components
        )
    }

    /**
     * Make Attribute.
     *
     * @param component
     * @return HashMap<String, Any>
     */
    fun makeAttributes(component: WfComponentEntity): LinkedHashMap<String, Any> {
        val attributes = LinkedHashMap<String, Any>()

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        attributes["type"] = component.componentType

        val common = LinkedHashMap<String, Any>()
        common["mapping-id"] = component.mappingId
        common["is-topic"] = component.isTopic
        attributes["common"] = common

        for (attribute in component.attributes!!) {
            val jsonElement = JsonParser().parse(attribute.attributeValue)
            when (jsonElement.isJsonArray) {
                true -> attributes[attribute.attributeId] = mapper.readValue(
                    attribute.attributeValue,
                    mapper.typeFactory.constructCollectionType(List::class.java, LinkedHashMap::class.java)
                )
                false -> attributes[attribute.attributeId] =
                    mapper.readValue(attribute.attributeValue, LinkedHashMap::class.java)
            }
        }

        return attributes
    }

    /**
     * Update Form.
     *
     * @param restTemplateFormDto
     * @return Boolean
     */
    fun updateForm(restTemplateFormDto: RestTemplateFormDto): Boolean {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(restTemplateFormDto.id)
        formEntity.get().formName = restTemplateFormDto.name
        formEntity.get().formDesc = restTemplateFormDto.desc
        formEntity.get().formStatus = restTemplateFormDto.status
        formEntity.get().updateDt = restTemplateFormDto.updateDt
        formEntity.get().updateUserKey = restTemplateFormDto.updateUserKey
        wfFormRepository.save(formEntity.get())
        return true
    }

    /**
     * Insert, Update Form Data.
     *
     * @param restTemplateFormComponentListDto
     */
    fun saveFormData(restTemplateFormComponentListDto: RestTemplateFormComponentListDto) {

        // Delete component, attribute
        val componentEntities = wfComponentRepository.findByFormId(restTemplateFormComponentListDto.formId)
        val componentIds: MutableList<String> = mutableListOf()
        for (component in componentEntities) {
            componentIds.add(component.componentId)
        }
        if (componentIds.isNotEmpty()) {
            wfComponentRepository.deleteComponentEntityByComponentIdIn(componentIds)
        }

        // Update Form
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val wfFormData: Optional<WfFormEntity> =
            wfFormRepository.findWfFormEntityByFormId(restTemplateFormComponentListDto.formId)
        wfFormData.ifPresent {
            wfFormData.get().formName = restTemplateFormComponentListDto.name
            wfFormData.get().formDesc = restTemplateFormComponentListDto.desc
            wfFormData.get().formStatus = restTemplateFormComponentListDto.status
            wfFormData.get().updateDt = restTemplateFormComponentListDto.updateDt
            wfFormData.get().updateUserKey = restTemplateFormComponentListDto.updateUserKey
            val resultFormEntity = wfFormRepository.save(wfFormData.get())

            // Insert component, attribute
            val wfComponentDataEntities: MutableList<WfComponentDataEntity> = mutableListOf()
            for (component in restTemplateFormComponentListDto.components) {
                var mappingId = ""
                var isTopic = false
                if (component.dataAttribute != null) {
                    val common: java.util.LinkedHashMap<*, *>? =
                        mapper.convertValue(component.dataAttribute, LinkedHashMap::class.java)
                    if (common != null) {
                        if (common.containsKey("mappingId")) {
                            mappingId = common["mappingId"] as String
                        }
                        if (common.containsKey("isTopic")) {
                            isTopic = common["isTopic"] as Boolean
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
                val resultComponentEntity = wfComponentRepository.save(componentEntity)

                // wf_comp_data 저장
                var componentDataEntity = WfComponentDataEntity(
                    componentId = resultComponentEntity.componentId,
                    attributeId = "display",
                    attributeValue = mapper.writeValueAsString(component.display),
                    attributes = resultComponentEntity
                )
                wfComponentDataEntities.add(componentDataEntity)

                component.label?.let {
                    if (it.size > 0) {
                        componentDataEntity = WfComponentDataEntity(
                            componentId = resultComponentEntity.componentId,
                            attributeId = "label",
                            attributeValue = mapper.writeValueAsString(it),
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
                            attributeValue = mapper.writeValueAsString(it),
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
                            attributeValue = mapper.writeValueAsString(it),
                            attributes = resultComponentEntity
                        )
                        wfComponentDataEntities.add(componentDataEntity)
                    }
                }
            }
            if (wfComponentDataEntities.isNotEmpty()) {
                wfComponentDataRepository.saveAll(wfComponentDataEntities)
            }
        }
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
            createUserKey = restTemplateFormComponentListDto.createUserKey,
            createDt = restTemplateFormComponentListDto.createDt
        )
        val wfFormDto = createForm(formDataDto)
        restTemplateFormComponentListDto.formId = wfFormDto.id
        when (restTemplateFormComponentListDto.status) {
            WfFormConstants.FormStatus.PUBLISH.value, WfFormConstants.FormStatus.DESTROY.value -> wfFormDto.editable =
                false
        }
        for (component in restTemplateFormComponentListDto.components) {
            component.componentId = UUID.randomUUID().toString().replace("-", "")
        }
        saveFormData(restTemplateFormComponentListDto)

        return wfFormDto
    }
}
