package co.brainz.workflow.engine.form.service

import co.brainz.workflow.engine.component.entity.WfComponentDataEntity
import co.brainz.workflow.engine.component.entity.WfComponentEntity
import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.form.constants.WfFormConstants
import co.brainz.workflow.engine.form.dto.WfFormComponentDataDto
import co.brainz.workflow.engine.form.dto.WfFormComponentSaveDto
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.dto.WfFormDto
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.mapper.WfFormMapper
import co.brainz.workflow.engine.form.repository.WfFormRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonParser
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID
import kotlin.collections.set

@Service
class WfFormService(
    private val wfFormRepository: WfFormRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfComponentDataRepository: WfComponentDataRepository
) {

    private val wfFormMapper: WfFormMapper = Mappers.getMapper(WfFormMapper::class.java)

    /**
     * Search Forms.
     *
     * @param parameters
     * @return List<FormDto>
     */
    fun forms(parameters: LinkedHashMap<String, Any>): List<WfFormDto> {
        var search = ""
        var status = listOf<String>()
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["status"] != null) status = parameters["status"].toString().split(",")
        // val formEntityList = formRepository.findFormEntityList(search, search)
        val formEntityList = if (status.isEmpty()) {
            wfFormRepository.findFormListOrFormSearchList(search)
        } else {
            wfFormRepository.findByFormStatusInOrderByFormName(status)
        }
        val formList = mutableListOf<WfFormDto>()
        for (item in formEntityList) {
            formList.add(formEntityToDto(item))
        }

        return formList
    }

    /**
     * Create Form.
     *
     * @param wfFormDto
     * @return FormDto
     */
    fun createForm(wfFormDto: WfFormDto): WfFormDto {
        val formEntity = WfFormEntity(
            formId = wfFormDto.id,
            formName = wfFormDto.name,
            formDesc = wfFormDto.desc,
            formStatus = wfFormDto.status,
            createDt = wfFormDto.createDt,
            createUserKey = wfFormDto.createUserKey
        )
        val dataEntity = wfFormRepository.save(formEntity)

        return WfFormDto(
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
    fun deleteForm(formId: String) {
        wfFormRepository.removeWfFormEntityByFormId(formId)
    }

    /**
     * Form Info.
     *
     * @param formId
     * @return WfFormDto
     */
    fun form(formId: String): WfFormDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId).get()
        val wfFormDto = wfFormMapper.toFormDto(formEntity)
        when (wfFormDto.status) {
            WfFormConstants.FormStatus.EDIT.value, WfFormConstants.FormStatus.PUBLISH.value -> wfFormDto.editable = true
        }
        return wfFormDto
    }

    /**
     * Form Data Info.
     *
     * @param formId
     * @return FormComponentDto
     */
    fun formData(formId: String): WfFormComponentViewDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId)
        val formViewDto = wfFormMapper.toFormViewDto(formEntity.get())
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        for (component in formEntity.get().components!!) {
            val attributes = LinkedHashMap<String, Any>()
            attributes["id"] = component.componentId
            attributes.putAll(makeAttributes(component))
            components.add(attributes)
        }

        return WfFormComponentViewDto(
            form = formViewDto,
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
     * @param wfFormDto
     * @return Boolean
     */
    fun updateForm(wfFormDto: WfFormDto): Boolean {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(wfFormDto.id)
        formEntity.get().formName = wfFormDto.name
        formEntity.get().formDesc = wfFormDto.desc
        formEntity.get().formStatus = wfFormDto.status
        formEntity.get().updateDt = wfFormDto.updateDt
        formEntity.get().updateUserKey = wfFormDto.updateUserKey
        wfFormRepository.save(formEntity.get())
        return true
    }

    /**
     * Insert, Update Form Data.
     *
     * @param wfFormComponentSaveDto
     */
    fun saveFormData(wfFormComponentSaveDto: WfFormComponentSaveDto) {

        // Delete component, attribute
        val componentEntities = wfComponentRepository.findByFormId(wfFormComponentSaveDto.form.id)
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
            wfFormRepository.findWfFormEntityByFormId(wfFormComponentSaveDto.form.id)
        wfFormData.ifPresent {
            wfFormData.get().formName = wfFormComponentSaveDto.form.name
            wfFormData.get().formDesc = wfFormComponentSaveDto.form.desc
            wfFormData.get().formStatus = wfFormComponentSaveDto.form.status
            wfFormData.get().updateDt = wfFormComponentSaveDto.form.updateDt
            wfFormData.get().updateUserKey = wfFormComponentSaveDto.form.updateUserKey
            val resultFormEntity = wfFormRepository.save(wfFormData.get())

            // Insert component, attribute
            val wfComponentDataEntities: MutableList<WfComponentDataEntity> = mutableListOf()
            for (component in wfFormComponentSaveDto.components) {
                var mappingId = ""
                if (component["common"] != null) {
                    val common: java.util.LinkedHashMap<*, *>? =
                        mapper.convertValue(component["common"], LinkedHashMap::class.java)
                    if (common != null) {
                        if (common.containsKey("mapping-id")) {
                            mappingId = common["mapping-id"] as String
                        }
                    }
                }
                val componentEntity = WfComponentEntity(
                    componentId = component["id"] as String,
                    componentType = component["type"] as String,
                    mappingId = mappingId,
                    form = resultFormEntity
                )
                val resultComponentEntity = wfComponentRepository.save(componentEntity)

                // wf_comp_data 저장
                for ((key, value) in component) {
                    if (key != "id" && key != "type" && key != "common") {
                        val componentDataEntity = WfComponentDataEntity(
                            componentId = resultComponentEntity.componentId,
                            attributeId = key,
                            attributeValue = mapper.writeValueAsString(value),
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
     * @param wfFormComponentSaveDto
     * @return WfFormDto
     */
    fun saveAsFormData(wfFormComponentSaveDto: WfFormComponentSaveDto): WfFormDto {
        val formDataDto = WfFormDto(
            name = wfFormComponentSaveDto.form.name,
            status = wfFormComponentSaveDto.form.status,
            desc = wfFormComponentSaveDto.form.desc,
            createUserKey = wfFormComponentSaveDto.form.createUserKey,
            createDt = wfFormComponentSaveDto.form.createDt
        )
        val wfFormDto = createForm(formDataDto)
        wfFormComponentSaveDto.form.id = wfFormDto.id
        when (wfFormComponentSaveDto.form.status) {
            WfFormConstants.FormStatus.PUBLISH.value, WfFormConstants.FormStatus.DESTROY.value -> wfFormDto.editable =
                false
        }
        for (component in wfFormComponentSaveDto.components) {
            component["id"] = UUID.randomUUID().toString().replace("-", "")
        }
        saveFormData(wfFormComponentSaveDto)

        return wfFormDto
    }

    /**
     * Entity -> Dto.
     *
     * @param wfFormEntity
     * @return FormDto
     */
    fun formEntityToDto(wfFormEntity: WfFormEntity): WfFormDto {
        val formDto = WfFormDto(
            id = wfFormEntity.formId,
            name = wfFormEntity.formName,
            status = wfFormEntity.formStatus,
            desc = wfFormEntity.formDesc,
            createUserKey = wfFormEntity.createUserKey,
            createDt = wfFormEntity.createDt,
            updateUserKey = wfFormEntity.updateUserKey,
            updateDt = wfFormEntity.updateDt
        )
        when (wfFormEntity.formStatus) {
            WfFormConstants.FormStatus.EDIT.value, WfFormConstants.FormStatus.PUBLISH.value -> formDto.editable = true
        }
        return formDto
    }

    /**
     * Get Component Data.
     *
     * @param componentType
     * @return List<WfFormComponentDataDto>
     */
    fun getFormComponentData(componentType: String): List<WfFormComponentDataDto> {
        val componentDataList = mutableListOf<WfFormComponentDataDto>()
        val componentDataEntityList = if (componentType == "") {
            wfComponentDataRepository.findAll()
        } else {
            wfComponentDataRepository.findByComponentDataList(componentType)
        }
        for (componentDataEntity in componentDataEntityList) {
            componentDataList.add(
                WfFormComponentDataDto(
                    componentId = componentDataEntity.componentId,
                    attributeId = componentDataEntity.attributeId,
                    attributeValue = componentDataEntity.attributeValue
                )
            )
        }
        return componentDataList
    }
}
