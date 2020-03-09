package co.brainz.workflow.form.service

import co.brainz.workflow.component.entity.ComponentDataEntity
import co.brainz.workflow.component.entity.ComponentEntity
import co.brainz.workflow.component.repository.ComponentDataRepository
import co.brainz.workflow.component.repository.ComponentRepository
import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.dto.FormComponentSaveDto
import co.brainz.workflow.form.dto.FormComponentViewDto
import co.brainz.workflow.form.dto.FormDto
import co.brainz.workflow.form.dto.FormViewDto
import co.brainz.workflow.form.entity.FormEntity
import co.brainz.workflow.form.repository.FormRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonParser
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.collections.set

@Service
class WFFormService(private val formMstRepository: FormRepository,
                    private val componentRepository: ComponentRepository,
                    private val componentDataRepository: ComponentDataRepository) : Form {

    /**
     * Search Forms.
     *
     * @param search
     * @return List<FormDto>
     */
    override fun forms(search: String): List<FormDto> {
        //val formEntityList = formRepository.findFormEntityList(search, search)
        val formEntityList = formMstRepository.findFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContainingOrderByCreateDtDesc(search, search)
        val formList = mutableListOf<FormDto>()
        for (item in formEntityList) {
            formList.add(formEntityToDto(item))
        }

        return formList
    }

    /**
     * Create Form.
     *
     * @param formDto
     * @return FormDto
     */
    override fun createForm(formDto: FormDto): FormDto {
        val formMstEntity = FormEntity(
                formId = formDto.formId,
                formName = formDto.formName,
                formDesc = formDto.formDesc,
                formStatus = formDto.formStatus,
                createDt = formDto.createDt,
                createUserKey = formDto.createUserKey
        )
        val dataEntity = formMstRepository.save(formMstEntity)

        return FormDto(
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
        formMstRepository.removeFormEntityByFormId(formId)
    }

    /**
     * Search Form.
     *
     * @param formId
     * @return FormComponentDto
     */
    override fun form(formId: String): FormComponentViewDto {
        val formEntity = formMstRepository.findFormEntityByFormId(formId)
        val formViewDto = FormViewDto(
                id = formEntity.get().formId,
                name = formEntity.get().formName,
                desc = formEntity.get().formDesc
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

        return FormComponentViewDto(
                form = formViewDto,
                components = components
        )

    }

    /**
     * Insert Form.
     *
     * @param formComponentSaveDto
     */
    override fun saveForm(formComponentSaveDto: FormComponentSaveDto) {

        //Delete component, attribute
        val componentMstEntities = componentRepository.findByFormId(formComponentSaveDto.form.formId)
        val componentIds: MutableList<String> = mutableListOf()
        for (componentMst in componentMstEntities) {
            componentIds.add(componentMst.componentId)
        }
        if (componentIds.isNotEmpty()) {
            componentRepository.deleteComponentMstEntityByComponentIdIn(componentIds)
        }

        //Update Form
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val formData: Optional<FormEntity> = formMstRepository.findFormEntityByFormId(formComponentSaveDto.form.formId)
        formData.ifPresent {
            formData.get().formName = formComponentSaveDto.form.formName
            formData.get().formDesc = formComponentSaveDto.form.formDesc
            formData.get().updateDt = formComponentSaveDto.form.updateDt
            formData.get().updateUserKey = formComponentSaveDto.form.updateUserKey
            val resultFormMstEntity = formMstRepository.save(formData.get())

            //Insert component, attribute
            val componentDataEntities: MutableList<ComponentDataEntity> = mutableListOf()
            for (component in formComponentSaveDto.components) {
                var mappingId = ""
                if (component["common"] != null) {
                    val common: java.util.LinkedHashMap<*, *>? = mapper.convertValue(component["common"], LinkedHashMap::class.java)
                    if (common != null) {
                        if (common.containsKey("mapping-id")) {
                            mappingId = common["mapping-id"] as String
                        }
                    }
                }
                val componentMstEntity = ComponentEntity(
                        componentId = component["id"] as String,
                        componentType = component["type"] as String,
                        mappingId = mappingId,
                        components = resultFormMstEntity
                )
                val resultComponentMstEntity = componentRepository.save(componentMstEntity)

                //wf_comp_data 저장
                for ((key, value) in component) {
                    if (key != "id" && key != "type" && key != "common") {
                        val componentDataEntity = ComponentDataEntity(
                                componentId = resultComponentMstEntity.componentId,
                                attributeId = key,
                                attributeValue = mapper.writeValueAsString(value),
                                attributes = resultComponentMstEntity
                        )
                        componentDataEntities.add(componentDataEntity)
                    }
                }
            }
            if (componentDataEntities.isNotEmpty()) {
                componentDataRepository.saveAll(componentDataEntities)
            }
        }

    }

    /**
     * Entity -> Dto.
     *
     * @param formEntity
     * @return FormDto
     */
    fun formEntityToDto(formEntity: FormEntity): FormDto {
        val formDto = FormDto(
                formId = formEntity.formId,
                formName = formEntity.formName,
                formStatus = formEntity.formStatus,
                formDesc = formEntity.formDesc,
                createUserKey = formEntity.createUserKey,
                createDt = formEntity.createDt,
                updateUserKey = formEntity.updateUserKey,
                updateDt = formEntity.updateDt
        )
        when (formEntity.formStatus) {
            FormConstants.FormStatus.EDIT.value, FormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
        }
        return formDto
    }

}
