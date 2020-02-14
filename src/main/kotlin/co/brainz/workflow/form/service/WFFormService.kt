package co.brainz.workflow.form.service

import co.brainz.workflow.component.dto.ComponentDto
import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.dto.FormComponentDto
import co.brainz.workflow.form.dto.FormDto
import co.brainz.workflow.form.dto.FormViewDto
import co.brainz.workflow.form.entity.FormMstEntity
import co.brainz.workflow.form.repository.FormMstRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class WFFormService(private val formMstRepository: FormMstRepository) : Form {

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
     * Search Form.
     *
     * @param formId
     * @return FormDto
     */
    override fun form(formId: String): FormDto {
        val formEntity = formMstRepository.findFormEntityByFormId(formId)
        return formEntityToDto(formEntity.get())
    }

    /**
     * Insert Form.
     *
     * @param formDto
     * @return FormDto
     */
    override fun insertForm(formDto: FormDto): FormDto {
        val formMstEntity = FormMstEntity(
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
     * Update Form.
     *
     * @param formDto
     */
    override fun updateForm(formDto: FormDto) {
        val formMstEntity: Optional<FormMstEntity> = formMstRepository.findFormEntityByFormId(formDto.formId)
        formMstEntity.ifPresent {
            formMstEntity.get().formName = formDto.formName
            formMstEntity.get().formDesc = formDto.formDesc
            formMstEntity.get().formStatus = formDto.formStatus
            formMstRepository.save(formMstEntity.get())
        }
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
     * Search Form + Components.
     *
     * @param formId
     * @return FormComponentDto
     */
    override fun formComponents(formId: String): FormComponentDto {
        val formEntity = formMstRepository.findFormEntityByFormId(formId)
        val formViewDto = FormViewDto(
                id = formEntity.get().formId,
                name = formEntity.get().formName,
                desc = formEntity.get().formDesc
        )
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        val mapper = ObjectMapper()
        for (component in formEntity.get().components!!) {
            val map = LinkedHashMap<String, Any>()
            map["id"] = component.compId
            map["type"] = component.compType

            //make common
            val common = LinkedHashMap<String, Any>()
            common["mapping-id"] = component.mappingId?:""
            map["common"] = common

            //attribute
            for (attribute in component.attributes!!) {
                map[attribute.attrId] = mapper.readValue(attribute.attrValue, LinkedHashMap::class.java)
            }
            components.add(map)
        }

        return FormComponentDto(
                form = formViewDto,
                components = components
        )

    }

    /**
     * Entity -> Dto.
     *
     * @param formMstEntity
     * @return FormDto
     */
    fun formEntityToDto(formMstEntity: FormMstEntity): FormDto {
        val formDto = FormDto(
                formId = formMstEntity.formId,
                formName = formMstEntity.formName,
                formStatus = formMstEntity.formStatus,
                formDesc = formMstEntity.formDesc,
                createUserKey = formMstEntity.createUserKey,
                createDt = formMstEntity.createDt,
                updateUserKey = formMstEntity.updateUserKey,
                updateDt = formMstEntity.updateDt
        )
        when (formMstEntity.formStatus) {
            FormConstants.FormStatus.EDIT.value, FormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
        }
        return formDto
    }

}
