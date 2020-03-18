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
import co.brainz.workflow.engine.form.dto.WfFormViewDto
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.repository.WfFormRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonParser
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.collections.set

@Service
class WfFormService(private val wfFormRepository: WfFormRepository,
                    private val wfComponentRepository: WfComponentRepository,
                    private val wfComponentDataRepository: WfComponentDataRepository) : WfForm {

    /**
     * Search Forms.
     *
     * @param search
     * @return List<FormDto>
     */
    override fun forms(search: String): List<WfFormDto> {
        //val formEntityList = formRepository.findFormEntityList(search, search)
        val formEntityList = wfFormRepository.findWfFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContainingOrderByCreateDtDesc(search, search)
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
    override fun createForm(wfFormDto: WfFormDto): WfFormDto {
        val formEntity = WfFormEntity(
                formId = wfFormDto.formId,
                formName = wfFormDto.formName,
                formDesc = wfFormDto.formDesc,
                formStatus = wfFormDto.formStatus,
                createDt = wfFormDto.createDt,
                createUserKey = wfFormDto.createUserKey
        )
        val dataEntity = wfFormRepository.save(formEntity)

        return WfFormDto(
                formId = dataEntity.formId,
                formName = dataEntity.formName,
                formDesc = dataEntity.formDesc,
                formEnabled = true,
                createUserKey = dataEntity.createUserKey,
                createDt = dataEntity.createDt,
                formStatus = dataEntity.formStatus
        )
    }

    /**
     * Delete Form.
     *
     * @param formId
     */
    override fun deleteForm(formId: String) {
        wfFormRepository.removeWfFormEntityByFormId(formId)
    }

    /**
     * Search Form.
     *
     * @param formId
     * @return FormComponentDto
     */
    override fun form(formId: String): WfFormComponentViewDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId)
        val formViewDto = WfFormViewDto(
                id = formEntity.get().formId,
                name = formEntity.get().formName,
                desc = formEntity.get().formDesc,
                status = formEntity.get().formStatus
        )
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        for (component in formEntity.get().components!!) {
            val map = LinkedHashMap<String, Any>()
            map["id"] = component.componentId
            map["type"] = component.componentType

            //make common
            val common = LinkedHashMap<String, Any>()
            common["mapping-id"] = component.mappingId
            map["common"] = common

            //attribute
            for (attribute in component.attributes!!) {
                val jsonElement = JsonParser().parse(attribute.attributeValue)
                when (jsonElement.isJsonArray) {
                    true -> map[attribute.attributeId] = mapper.readValue(attribute.attributeValue, mapper.typeFactory.constructCollectionType(List::class.java, LinkedHashMap::class.java))
                    false -> map[attribute.attributeId] = mapper.readValue(attribute.attributeValue, LinkedHashMap::class.java)
                }
            }
            components.add(map)
        }

        return WfFormComponentViewDto(
                form = formViewDto,
                components = components
        )

    }

    /**
     * Insert Form.
     *
     * @param wfFormComponentSaveDto
     */
    override fun saveForm(wfFormComponentSaveDto: WfFormComponentSaveDto) {

        //Delete component, attribute
        val componentEntities = wfComponentRepository.findByFormId(wfFormComponentSaveDto.form.formId)
        val componentIds: MutableList<String> = mutableListOf()
        for (component in componentEntities) {
            componentIds.add(component.componentId)
        }
        if (componentIds.isNotEmpty()) {
            wfComponentRepository.deleteComponentEntityByComponentIdIn(componentIds)
        }

        //Update Form
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val wfFormData: Optional<WfFormEntity> = wfFormRepository.findWfFormEntityByFormId(wfFormComponentSaveDto.form.formId)
        wfFormData.ifPresent {
            wfFormData.get().formName = wfFormComponentSaveDto.form.formName
            wfFormData.get().formDesc = wfFormComponentSaveDto.form.formDesc
            wfFormData.get().formStatus = wfFormComponentSaveDto.form.formStatus
            wfFormData.get().updateDt = wfFormComponentSaveDto.form.updateDt
            wfFormData.get().updateUserKey = wfFormComponentSaveDto.form.updateUserKey
            val resultFormEntity = wfFormRepository.save(wfFormData.get())

            //Insert component, attribute
            val wfComponentDataEntities: MutableList<WfComponentDataEntity> = mutableListOf()
            for (component in wfFormComponentSaveDto.components) {
                var mappingId = ""
                if (component["common"] != null) {
                    val common: java.util.LinkedHashMap<*, *>? = mapper.convertValue(component["common"], LinkedHashMap::class.java)
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

                //wf_comp_data 저장
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
    override fun saveAsForm(wfFormComponentSaveDto: WfFormComponentSaveDto): WfFormDto {
        val formDataDto = WfFormDto(
                formName = wfFormComponentSaveDto.form.formName,
                formDesc = wfFormComponentSaveDto.form.formDesc,
                createUserKey = wfFormComponentSaveDto.form.createUserKey,
                createDt = wfFormComponentSaveDto.form.createDt,
                formStatus = wfFormComponentSaveDto.form.formStatus
        )
        val wfFormDto = createForm(formDataDto)
        wfFormComponentSaveDto.form.formId = wfFormDto.formId
        when (wfFormComponentSaveDto.form.formStatus) {
            WfFormConstants.FormStatus.PUBLISH.value, WfFormConstants.FormStatus.DESTROY.value -> wfFormDto.formEnabled = false
        }
        saveForm(wfFormComponentSaveDto)

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
                formId = wfFormEntity.formId,
                formName = wfFormEntity.formName,
                formStatus = wfFormEntity.formStatus,
                formDesc = wfFormEntity.formDesc,
                createUserKey = wfFormEntity.createUserKey,
                createDt = wfFormEntity.createDt,
                updateUserKey = wfFormEntity.updateUserKey,
                updateDt = wfFormEntity.updateDt
        )
        when (wfFormEntity.formStatus) {
            WfFormConstants.FormStatus.EDIT.value, WfFormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
        }
        return formDto
    }

    /**
     * Get Component Data.
     *
     * @param componentType
     * @param attributeId
     */
    override fun getFormComponentData(componentType: String, attributeId: String): List<WfFormComponentDataDto> {
        val componentDataList = mutableListOf<WfFormComponentDataDto>()
        val componentDataEntityList = wfComponentDataRepository.findByAttributeValueList(componentType, attributeId)
        for (componentDataEntity in componentDataEntityList) {
            componentDataList.add(WfFormComponentDataDto(
                    componentId = componentDataEntity.componentId,
                    attributeId = componentDataEntity.attributeId,
                    attributeValue = componentDataEntity.attributeValue
            ))
        }
        return componentDataList
    }
}