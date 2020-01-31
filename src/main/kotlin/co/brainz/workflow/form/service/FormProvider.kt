package co.brainz.workflow.form.service

import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.dto.FormDto
import co.brainz.workflow.form.entity.FormEntity
import co.brainz.workflow.form.repository.FormRepository
import org.springframework.stereotype.Service
import java.util.Optional


@Service
class FormProvider(private val formRepository: FormRepository) : Form {

    override fun formList(search: String): List<FormDto> {
        val formEntityList = formRepository.findFormEntityList(search, search)
        val formList = mutableListOf<FormDto>()
        for (item in formEntityList) {
            formList.add(formEntityToDto(item))
        }

        return formList
    }

    override fun form(formId: String): FormDto {
        val formEntity = formRepository.findFormEntityByFormId(formId)
        return formEntityToDto(formEntity.get())
    }

    override fun insertForm(formDto: FormDto) {
        val formEntity = FormEntity(
                formId = formDto.formStatus,
                formName = formDto.formName,
                formDesc = formDto.formDesc,
                formStatus = formDto.formStatus
        )
        formRepository.save(formEntity)
    }

    override fun updateForm(formDto: FormDto) {
        val formEntity: Optional<FormEntity> = formRepository.findFormEntityByFormId(formDto.formId)
        formEntity.ifPresent {
            formEntity.get().formName = formDto.formName
            formEntity.get().formDesc = formDto.formDesc
            formEntity.get().formStatus = formDto.formStatus
            formRepository.save(formEntity.get())
        }
    }

    override fun deleteForm(formId: String) {
        formRepository.removeFormEntityByFormId(formId)
    }

    fun formEntityToDto(formEntity: FormEntity): FormDto {
        val formDto = FormDto(
                formId = formEntity.formId,
                formName = formEntity.formName,
                formStatus = formEntity.formStatus,
                formDesc = formEntity.formDesc,
                createUserKey = formEntity.createUserkey,
                createDt = formEntity.createDt,
                updateUserKey = formEntity.updateUserkey,
                updateDt = formEntity.updateDt
        )
        when (formEntity.formStatus) {
            FormConstants.FormStatus.EDIT.value, FormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
        }
        return formDto
    }

}
