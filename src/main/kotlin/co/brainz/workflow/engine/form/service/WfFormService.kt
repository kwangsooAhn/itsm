package co.brainz.workflow.engine.form.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.workflow.engine.component.entity.WfComponentDataEntity
import co.brainz.workflow.engine.component.entity.WfComponentEntity
import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.form.constants.WfFormConstants
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.mapper.WfFormMapper
import co.brainz.workflow.engine.form.repository.WfFormRepository
import co.brainz.workflow.provider.dto.RestTemplateFormComponentSaveDto
import co.brainz.workflow.provider.dto.RestTemplateFormComponentViewDto
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
    private val wfDocumentRepository: WfDocumentRepository,
    private val aliceUserRepository: AliceUserRepository
) {

    private val wfFormMapper: WfFormMapper = Mappers.getMapper(WfFormMapper::class.java)

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
     * Form Data Info.
     *
     * @param formId
     * @return FormComponentDto
     */
    fun formData(formId: String): RestTemplateFormComponentViewDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId)
        val formViewDto = wfFormMapper.toFormViewDto(formEntity.get())
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        for (component in formEntity.get().components!!) {
            val attributes = LinkedHashMap<String, Any>()
            attributes["id"] = component.componentId
            attributes.putAll(makeAttributes(component))
            components.add(attributes)
        }

        return RestTemplateFormComponentViewDto(
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
        formEntity.get().updateUser = restTemplateFormDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        wfFormRepository.save(formEntity.get())
        return true
    }

    /**
     * Insert, Update Form Data.
     *
     * @param restTemplateFormComponentSaveDto
     */
    fun saveFormData(restTemplateFormComponentSaveDto: RestTemplateFormComponentSaveDto) {

        // Delete component, attribute
        val componentEntities = wfComponentRepository.findByFormId(restTemplateFormComponentSaveDto.form.id)
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
            wfFormRepository.findWfFormEntityByFormId(restTemplateFormComponentSaveDto.form.id)
        wfFormData.ifPresent {
            wfFormData.get().formName = restTemplateFormComponentSaveDto.form.name
            wfFormData.get().formDesc = restTemplateFormComponentSaveDto.form.desc
            wfFormData.get().formStatus = restTemplateFormComponentSaveDto.form.status
            wfFormData.get().updateDt = restTemplateFormComponentSaveDto.form.updateDt
            wfFormData.get().updateUser = restTemplateFormComponentSaveDto.form.updateUserKey?.let {
                aliceUserRepository.findAliceUserEntityByUserKey(it)
            }
            val resultFormEntity = wfFormRepository.save(wfFormData.get())

            // Insert component, attribute
            val wfComponentDataEntities: MutableList<WfComponentDataEntity> = mutableListOf()
            for (component in restTemplateFormComponentSaveDto.components) {
                var mappingId = ""
                var isTopic = false
                if (component["common"] != null) {
                    val common: java.util.LinkedHashMap<*, *>? =
                        mapper.convertValue(component["common"], LinkedHashMap::class.java)
                    if (common != null) {
                        if (common.containsKey("mapping-id")) {
                            mappingId = common["mapping-id"] as String
                        }
                        if (common.containsKey("is-topic")) {
                            isTopic = common["is-topic"] as Boolean
                        }
                    }
                }
                val componentEntity = WfComponentEntity(
                    componentId = component["id"] as String,
                    componentType = component["type"] as String,
                    mappingId = mappingId,
                    isTopic = isTopic,
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
     * @param restTemplateFormComponentSaveDto
     * @return RestTemplateFormDto
     */
    fun saveAsFormData(restTemplateFormComponentSaveDto: RestTemplateFormComponentSaveDto): RestTemplateFormDto {
        val formDataDto = RestTemplateFormDto(
            name = restTemplateFormComponentSaveDto.form.name,
            status = restTemplateFormComponentSaveDto.form.status,
            desc = restTemplateFormComponentSaveDto.form.desc,
            createUserKey = restTemplateFormComponentSaveDto.form.createUserKey,
            createDt = restTemplateFormComponentSaveDto.form.createDt
        )
        val wfFormDto = createForm(formDataDto)
        restTemplateFormComponentSaveDto.form.id = wfFormDto.id
        when (restTemplateFormComponentSaveDto.form.status) {
            WfFormConstants.FormStatus.PUBLISH.value, WfFormConstants.FormStatus.DESTROY.value -> wfFormDto.editable =
                false
        }
        for (component in restTemplateFormComponentSaveDto.components) {
            component["id"] = UUID.randomUUID().toString().replace("-", "")
        }
        saveFormData(restTemplateFormComponentSaveDto)

        return wfFormDto
    }
}
