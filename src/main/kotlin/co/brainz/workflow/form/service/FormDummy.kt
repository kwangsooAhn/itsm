package co.brainz.workflow.form.service

import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.dto.FormDto
import java.time.LocalDateTime
import java.util.UUID

class FormDummy: Form {

    override fun formList(search: String): List<FormDto> {
        val list = mutableListOf<FormDto>()
        if (search.isEmpty()) {
            val form = FormDto(
                    formId = UUID.randomUUID().toString(),
                    formName = "단순문의",
                    formDesc = "단순한 문의사항 접수하는 문서양식",
                    formStatus = FormConstants.FormStatus.EDIT.value,
                    formEnabled = true,
                    createDt = LocalDateTime.of(2019, 12, 31, 9, 50, 20, 0),
                    createUserkey = "f65d114e29664e7bbf1d47075379bbb5"
            )
            list.add(form)
        }

        val form2 = FormDto(
                formId = UUID.randomUUID().toString(),
                formName = "장애신고",
                formDesc = "서비스 장애관련 문의사항을 접수하는 문서양식",
                formStatus = FormConstants.FormStatus.SIMULATION.value,
                formEnabled = true,
                createDt = LocalDateTime.of(2019, 12, 13, 12, 30, 40, 0),
                createUserkey = "f65d114e29664e7bbf1d47075379bbb5",
                updateDt = LocalDateTime.of(2020, 1, 9, 17, 35, 40, 0),
                updateUserkey = "b0b3209dbe4042498603df7b216c4598"
        )
        list.add(form2)

        val form3 = FormDto(
                formId = UUID.randomUUID().toString(),
                formName = "인프라 변경",
                formDesc = "인프라 변경 관련 사항을 접수하는 문서양식",
                formStatus = FormConstants.FormStatus.PUBLISH.value,
                createDt = LocalDateTime.of(2019, 8, 10, 12, 30, 40, 0),
                createUserkey = "b0b3209dbe4042498603df7b216c4598",
                updateDt = LocalDateTime.of(2019, 12, 1, 17, 35, 40, 0),
                updateUserkey = "b0b3209dbe4042498603df7b216c4598"
        )
        list.add(form3)

        val form4 = FormDto(
                formId = UUID.randomUUID().toString(),
                formName = "인프라 변경 관리22",
                formDesc = "인프라 변경 관련 사항을 접수하는 문서양식22",
                formStatus = FormConstants.FormStatus.DESTROY.value,
                createDt = LocalDateTime.of(2019, 8, 10, 12, 30, 40, 0),
                createUserkey = "529a9128d7e74e13a3e57e3075566c5e"
        )
        list.add(form4)

        return list
    }

    override fun form(formId: String): FormDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertForm(formDto: FormDto) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateForm(formDto: FormDto) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteForm(formId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
