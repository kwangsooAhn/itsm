package co.brainz.itsm.document.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/documents")
class DocumentRestController {


    /**
     * 신청서의 문서 데이터 조회.
     *
     * @param documentId
     * */
    @GetMapping("/data/{documentId}")
    fun getTicket(@PathVariable documentId: String): String {
        // 테스트용 문서양식 데이터
        return """
               {
                "document": {"id": "$documentId", "name": "장애신고~", "description": "장애신고 신청서 문서양식입니다."},
                "components": [{
                    "id": "4a417b48-be2e-4ebe-82bf-8f80a63622a4",
                    "type": "text",
                    "label": {"position": "left", "column": 2, "size": 16, "color": "#000000", "bold": "N",
                    "italic": "Y", "underline": "Y", "text": "문자", "align": "center"},
                    "display": {"placeholder": "문자를 입력하세요.", "column": 10, "outline-width": 1,
                    "outline-color": "#000000", "order": 4},
                    "validate": {"required": "Y", "regexp": "char", "regexp-msg": "문자를 입력해주세요.",
                    "length-min": 5, "length-max": 30}
                },
                {
                    "id": "4a417b48-be2e-4ebe-82bf-8f80a63622a5",
                    "type": "text",
                    "label": {"position": "top", "column": 2, "size": 20, "color": "#000000", "bold": "Y",
                    "italic": "N", "underline": "N", "text": "숫자 :", "align": "left"},
                    "display": {"placeholder": "숫자를 입력하세요.", "column": 10, "outline-width": 2,
                    "outline-color": "#111111", "order": 6},
                    "validate": {"required": "N", "regexp": "num", "regexp-msg": "숫자를 입력해주세요.",
                    "length-min": 0, "length-max": 30}
                },
                {
                    "id": "4a417b48-be2e-4ebe-82bf-8f80a63622a6",
                    "type": "text",
                    "label": {"position": "left", "column": 4, "size": 15, "color": "#0f3fef", "bold": "Y",
                    "italic": "N", "underline": "N", "text": "숫자 + 문자 :", "align": "left"},
                    "display": {"placeholder": "숫자 + 문자를 입력하세요.", "column": 8, "outline-width": 1,
                    "outline-color": "#ff0000", "order": 3},
                    "validate": {"required": "N", "regexp": "numchar", "regexp-msg": "숫자 + 문자를 입력해주세요.",
                     "length-min": 0, "length-max": 30}
                },
                {
                    "id": "4a417b48-be2e-4ebe-82bf-8f80a63622a7",
                    "type": "text",
                    "label": {"position": "hidden", "column": 2, "size": 15, "color": "#000000", "bold": "N",
                    "italic": "N", "underline": "N", "text": "phone(hidden) :", "align": "left"},
                    "display": {"placeholder": "phone(hidden)를 입력하세요.", "column": 10, "outline-width": 1,
                    "outline-color": "#111111", "order": 7},
                    "validate": {"required": "N", "regexp": "phone", "regexp-msg": "phone을 입력해주세요.",
                    "length-min":0, "length-max": 30}
                },
                {
                    "id": "4a417b48-be2e-4ebe-82bf-8f80a63622a8",
                    "type": "text",
                    "label": {"position": "left", "column": 2, "size": 15, "color": "#000000", "bold": "N",
                    "italic": "N", "underline": "N", "text": "email(hidden) :", "align": "right"},
                    "display": {"placeholder": "email(hidden)를 입력하세요.", "column": 10, "outline-width": 1,
                    "outline-color": "#ff0000", "order": 9},
                    "validate": {"required": "N", "regexp": "email", "regexp-msg": "email을 입력해주세요.",
                    "length-min":0, "length-max": 30}
                },
                {
                    "id": "00e3b10a-a9b1-4f68-9ae0-113f34532c29",
                    "type": "textarea",
                    "common": {"mapping-id": ""},
                    "label": {"position": "left", "column": 2, "size": 12, "color": "#ff0000", "bold": "Y", "italic":
                     "N", "underline": "N", "text": "설명~~", "align": "left"},
                    "display": {"rows": 3, "placeholder": "~~~", "column": 10, "outline-width": 1, "outline-color":
                    "#000000", "order": 8},
                    "validate": {"required": "Y", "length-min": 0, "length-max": 100}
                },
                {
                    "id": "53f7b7f9-c7de-4ffc-a819-0be1d37032fe",
                    "type": "select",
                    "common": {"mapping-id": ""},
                    "label": {"position": "left", "column": 2, "size": 12, "color": "#000000", "bold": "Y", "italic":
                    "N", "underline": "N", "text": "부서", "align": "left"},
                    "display": {"column": 10, "order": 1},
                    "validate": {"required": "N"},
                    "option": [ {"seq": 1, "name": "ITSM팀", "value": "itsm"},
                                {"seq": 3, "name": "인프라웹팀", "value": "infraweb"},
                                {"seq": 2, "name": "UX팀", "value": "ux"} ]
                },
                {
                    "id": "53f7b7f9-c7de-4ffc-a819-0be1d37032ff",
                    "type": "radio",
                    "common": {"mapping-id": ""},
                    "label": {"position": "top", "column": 2, "size": 12, "color": "#000000", "bold": "Y", "italic":
                    "N", "underline": "N", "text": "프로젝트", "align": "left"},
                    "display": {"column": 10, "direction": "horizontal", "position": "left","order": 2},
                    "validate": {"required": "N"},
                    "option": [ {"seq": 1, "name": "ITSM", "value": "itsm"},
                                {"seq": 2, "name": "대시보드", "value": "dashboard"} ]
                },
                {
                    "id": "53f7b7f9-c7de-4ffc-a819-0be1d37032ff",
                    "type": "radio",
                    "common": {"mapping-id": ""},
                    "label": {"position": "left", "column": 2, "size": 15, "color": "#000000", "bold": "Y", "italic":
                    "N", "underline": "N", "text": "프로젝트~~", "align": "left"},
                    "display": {"column": 10, "direction": "vertical", "position": "right","order": 5},
                    "validate": {"required": "N"},
                    "option": [{"seq": 2, "name": "ITSM", "value": "itsm2"},
                               {"seq": 1, "name": "대시보드", "value": "dashboard2"}]
                }]
                }
                """
    }
}