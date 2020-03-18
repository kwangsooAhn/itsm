package co.brainz.workflow.engine.form.service

import co.brainz.workflow.engine.form.constants.WfFormConstants
import co.brainz.workflow.engine.form.dto.WfFormComponentDataDto
import co.brainz.workflow.engine.form.dto.WfFormComponentSaveDto
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.dto.WfFormDto
import java.time.LocalDateTime
import java.util.UUID

class WfFormDummy: WfForm {

    override fun forms(search: String): List<WfFormDto> {
        val list = mutableListOf<WfFormDto>()
        if (search.isEmpty()) {
            val form = WfFormDto(
                    formId = UUID.randomUUID().toString(),
                    formName = "단순문의",
                    formDesc = "단순한 문의사항 접수하는 문서양식",
                    formStatus = WfFormConstants.FormStatus.EDIT.value,
                    formEnabled = true,
                    createDt = LocalDateTime.of(2019, 12, 31, 9, 50, 20, 0),
                    createUserKey = "f65d114e29664e7bbf1d47075379bbb5"
            )
            list.add(form)
        }

        val form2 = WfFormDto(
                formId = UUID.randomUUID().toString(),
                formName = "장애신고",
                formDesc = "서비스 장애관련 문의사항을 접수하는 문서양식",
                formStatus = WfFormConstants.FormStatus.SIMULATION.value,
                formEnabled = true,
                createDt = LocalDateTime.of(2019, 12, 13, 12, 30, 40, 0),
                createUserKey = "f65d114e29664e7bbf1d47075379bbb5",
                updateDt = LocalDateTime.of(2020, 1, 9, 17, 35, 40, 0),
                updateUserKey = "b0b3209dbe4042498603df7b216c4598"
        )
        list.add(form2)

        val form3 = WfFormDto(
                formId = UUID.randomUUID().toString(),
                formName = "인프라 변경",
                formDesc = "인프라 변경 관련 사항을 접수하는 문서양식",
                formStatus = WfFormConstants.FormStatus.PUBLISH.value,
                createDt = LocalDateTime.of(2019, 8, 10, 12, 30, 40, 0),
                createUserKey = "b0b3209dbe4042498603df7b216c4598",
                updateDt = LocalDateTime.of(2019, 12, 1, 17, 35, 40, 0),
                updateUserKey = "b0b3209dbe4042498603df7b216c4598"
        )
        list.add(form3)

        val form4 = WfFormDto(
                formId = UUID.randomUUID().toString(),
                formName = "인프라 변경 관리22",
                formDesc = "인프라 변경 관련 사항을 접수하는 문서양식22",
                formStatus = WfFormConstants.FormStatus.DESTROY.value,
                createDt = LocalDateTime.of(2019, 8, 10, 12, 30, 40, 0),
                createUserKey = "529a9128d7e74e13a3e57e3075566c5e"
        )
        list.add(form4)

        return list
    }

    override fun form(formId: String): WfFormComponentViewDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createForm(wfFormDto: WfFormDto): WfFormDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteForm(formId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveForm(wfFormComponentSaveDto: WfFormComponentSaveDto) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFormComponentData(componentType: String, attributeId: String): List<WfFormComponentDataDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
