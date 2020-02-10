package co.brainz.workflow.form.service

import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.dto.FormDto
import co.brainz.workflow.form.entity.FormMstEntity
import co.brainz.workflow.form.repository.FormMstMstRepository
import org.springframework.stereotype.Service
import java.util.Optional


@Service
class WFFormService(private val formMstRepository: FormMstMstRepository) : Form {

    /**
     * Search FormList.
     *
     * @param search
     * @return List<FormDto>
     */
    override fun formList(search: String): List<FormDto> {
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
                createUserkey = formDto.createUserkey
        )
        val dataEntity = formMstRepository.save(formMstEntity)

        return FormDto(
                formId = dataEntity.formId,
                formName = dataEntity.formName,
                formDesc = dataEntity.formDesc,
                formEnabled = true,
                createUserkey = dataEntity.createUserkey,
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

    fun formEntityToDto(formMstEntity: FormMstEntity): FormDto {
        val formDto = FormDto(
                formId = formMstEntity.formId,
                formName = formMstEntity.formName,
                formStatus = formMstEntity.formStatus,
                formDesc = formMstEntity.formDesc,
                createUserkey = formMstEntity.createUserkey,
                createDt = formMstEntity.createDt,
                updateUserkey = formMstEntity.updateUserkey,
                updateDt = formMstEntity.updateDt
        )
        when (formMstEntity.formStatus) {
            FormConstants.FormStatus.EDIT.value, FormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
        }
        return formDto
    }

}
