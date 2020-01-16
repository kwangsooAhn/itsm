package co.brainz.itsm.form.service

import co.brainz.itsm.form.dto.FormDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class FormService {

    /**
     * 폼 데이터 조회.
     */
    fun findFormList(search: String): MutableList<Map<String, String?>> {
        //TODO DB에서 리스트 조회. like 검색 필요.
        //임시 데이터
        val formList = mutableListOf<Map<String, String?>>()
        if (search.isEmpty()) {
            val form = mutableMapOf<String, String?>()
            form["form_id"] = "1"
            form["form_name"] = "단순문의"
            form["form_desc"] = "단순한 문의사항 접수하는 문서양식"
            form["create_dt"] = "2019-12-31 09:50:20"
            form["create_user_name"] = "이소현"
            form["update_dt"] = null
            form["update_user_name"] = null
            form["status"] = "form.status.edit"
            form["disabled"] = "false"
            formList.add(form)
        }
        val form2 = mutableMapOf<String, String>()
        form2["form_id"] = "2"
        form2["form_name"] = "장애신고"
        form2["form_desc"] = "서비스 장애관련 문의사항을 접수하는 문서양식"
        form2["create_dt"] = "2019-12-13 12:30:40"
        form2["create_user_name"] = "이소현"
        form2["update_dt"] = "2020-01-09 17:35:40"
        form2["update_user_name"] = "관리자"
        form2["status"] = "form.status.simu"
        form2["disabled"] = "false"
        formList.add(form2)

        val form3 = mutableMapOf<String, String>()
        form3["form_id"] = "3"
        form3["form_name"] = "인프라 변경 관리"
        form3["form_desc"] = "인프라 변경 관련 사항을 접수하는 문서양식"
        form3["create_dt"] = "2019-08-10 12:30:40"
        form3["create_user_name"] = "관리자"
        form3["update_dt"] = "2019-12-01 17:35:40"
        form3["update_user_name"] = "관리자"
        form3["status"] = "form.status.publish"
        form3["disabled"] = "true"
        formList.add(form3)

        val form4 = mutableMapOf<String, String?>()
        form4["form_id"] = "4"
        form4["form_name"] = "인프라 변경 관리22"
        form4["form_desc"] = "인프라 변경 관련 사항을 접수하는 문서양식22"
        form4["create_dt"] = "2019-08-10 12:30:40"
        form4["create_user_name"] = "관리자22"
        form4["update_dt"] = null
        form4["update_user_name"] = null
        form4["status"] = "form.status.destroy"
        form4["disabled"] = "true"
        formList.add(form4)

        return formList
    }

    /**
     * 폼 신규 기본 정보 등록.
     */
    fun insertForm(formDto: FormDto): String {
        //TODO DB에 저장.
        val userName: String = SecurityContextHolder.getContext().authentication.name //사용자 이름

        //등록된 form_id return
        return "100"
    }

    /**
     * 폼 1건 데이터 삭제.
     */
    fun deleteFrom(formId: String) {
        //TODO DB에서 삭제.
    }
}