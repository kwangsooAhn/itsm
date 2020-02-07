package co.brainz.workflow.form.service

import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.dto.FormDto
import co.brainz.workflow.form.entity.FormEntity
import co.brainz.workflow.form.repository.FormRepository
import co.brainz.workflow.process.ProcessDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import java.util.*

@Service
class WFFormService(private val formRepository: FormRepository) : Form {

    override fun formList(search: String): List<FormDto> {
        //val formEntityList = formRepository.findFormEntityList(search, search)
        val formEntityList = formRepository.findFormEntityByFormNameIgnoreCaseContainingOrFormDescIgnoreCaseContaining(search, search)
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

    override fun insertForm(multiValueMap: LinkedMultiValueMap<String, Any>) {
        println(">>>>>>>>>>")
        println(multiValueMap)
        /*val formDto = FormDto(
                formName = multiValueMap.
        )*/

        val objectMapper = ObjectMapper()
        /*val map: Map<String, String> = objectMapper.convertValue(multiValueMap, object : TypeReference<Map<String?, String?>?>() {})
        println(map)*/



        //val list: List<FormDto> = objectMapper.readValue(multiValueMap, objectMapper.typeFactory.constructCollectionType(List::class.java, FormDto::class.java))

        /*var formDto = FormDto()
        val itr = multiValueMap.keys.iterator()
        while (itr.hasNext()) {
            val key = itr.next()
            val fields = FormDto::class.java.getDeclaredFields()
            for (filed in fields) {
                if (filed.name == key) {

                }
            }
        }*/


        val a = objectMapper.convertValue(multiValueMap, FormDto::class.java)
        //objectMapper.convertValue(multiValueMap, object : TypeReference<FormDto>>() {})
        println(a)
        println("aaaaaaaaaaaa")
    }

    //map의 key를 돌리면서... formDto에 값을 셋팅한다.
    //공통 컨버터가 있을까??
    //없으면.. 만들어서?



    /*override fun insertForm(formDto: FormDto) {
        println(">xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx><<<<<<<<")
        val formEntity = FormEntity(
                formId = formDto.formStatus,
                formName = formDto.formName,
                formDesc = formDto.formDesc,
                formStatus = formDto.formStatus,
                aliceUserEntity = null
        )
        formRepository.save(formEntity)
    }*/

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
                createUserkey = formEntity.createUserkey,
                createDt = formEntity.createDt,
                updateUserkey = formEntity.updateUserkey,
                updateDt = formEntity.updateDt,
                userName = formEntity.aliceUserEntity!!.userName
        )
        when (formEntity.formStatus) {
            FormConstants.FormStatus.EDIT.value, FormConstants.FormStatus.SIMULATION.value -> formDto.formEnabled = true
        }
        return formDto
    }

}
