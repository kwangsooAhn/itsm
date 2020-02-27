package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.provider.dto.TokenSaveDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

@RestController
@RequestMapping("/rest/documents")
class DocumentRestController(private val documentService: DocumentService) {

    /**
     * 신청서의 문서 데이터 조회.
     *
     * @param documentId
     * */
    @GetMapping("/data/{documentId}")
    fun getTicket(@PathVariable documentId: String): String {
        if (documentId != "32ds1261420w7edbcd5251d7b24a6c23") {
            return dummyData(documentId)
        }
        return documentService.findDocument(documentId)
    }

    /**
     * Dummy Data. 구현 완료 시 삭제
     *
     * */
    fun dummyData(documentId: String): String {
        return """
               {
                "document": {"id": "$documentId", "name": "회원가입~", "description": "회원가입 문서양식입니다."},
                "components": [{
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703201",
                    "type": "label",
                    "common": {"mapping-id": ""},
                    "display": {
                        "size": 30, "color": "#000000", "bold": "Y", "italic": "Y", "underline": "N", 
                        "text": "회원가입 문서양식", "align": "center", "order": 1}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703202",
                    "type": "line",
                    "common": {"mapping-id": ""},
                    "display": {"width": 2, "color": "#0066ff", "type": "dashed", "order": 2}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703203",
                    "type": "text",
                    "label": {
                        "position": "left", "column": 2, "size": 16, "color": "#000000", "bold": "N", "italic": "Y", 
                        "underline": "N", "text": "아이디", "align": "left"},
                    "display": {
                        "placeholder": "아이디를 입력하세요.", "column": 10, "outline-width": 1, "outline-color": "#000000", 
                        "order": 3},
                    "validate": {
                        "required": "Y", "regexp": "char", "regexp-msg": "문자를 입력해주세요.", "length-min": 0, 
                        "length-max": 30}
                },
                {
                    "id": "53f7b7f9-c7de-4ffc-a819-0be1d3703204",
                    "type": "text",
                    "label": {
                        "position": "left", "column": 2, "size": 16, "color": "#000000", "bold": "Y", "italic": "N", 
                        "underline": "Y", "text": "이름", "align": "center"},
                    "display": {
                        "placeholder": "이름을 입력하세요.", "column": 10, "outline-width": 1, "outline-color": "#000000", 
                        "order": 4},
                    "validate": {
                        "required": "Y", "regexp": "num", "regexp-msg": "숫자를 입력해주세요.", "length-min": 0, 
                        "length-max": 5}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703124",
                    "type": "text",
                    "label": {
                        "position": "left", "column": 2, "size": 16, "color": "#000000", "bold": "Y", "italic": "N", 
                        "underline": "Y", "text": "이메일", "align": "right"},
                    "display": {
                        "placeholder": "이메일을 입력하세요.", "column": 10, "outline-width": 1, "outline-color": "#000000", 
                        "order": 4},
                    "validate": {
                        "required": "N", "regexp": "email", "regexp-msg": "이메일을 입력해주세요.", "length-min": 0, 
                        "length-max": 10}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d37032105",
                    "type": "textarea",
                    "label": {
                        "position": "hidden", "column": 2, "size": 16, "color": "#000000", "bold": "Y", "italic": "N", 
                        "underline": "Y", "text": "기타", "align": "left"},
                    "display": {
                        "placeholder": "기타사항을 입력하세요.", "column": 10, "outline-width": 1, "outline-color": "#cc66ff", 
                        "order": 5},
                    "validate": {"required": "N", "regexp": "none", "regexp-msg": "", "length-min": 5, "length-max": 30}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703205",
                    "type": "select",
                    "common": {"mapping-id": ""},
                    "label": {
                        "position": "left", "column": 2, "size": 12, "color": "#66ccff", "bold": "Y", "italic": "N", 
                        "underline": "N", "text": "날짜 유형", "align": "left"},
                    "display": {"column": 10, "order": 5},
                    "validate": {"required": "Y"},
                    "option": [ {"seq": 4, "name": "dd-MM-yyyy", "value": "ddmmyyyy"},
                                {"seq": 1, "name": "yyyy-MM-dd", "value": "yyyymmdd"},
                                {"seq": 3, "name": "MM-dd-yyyy", "value": "mmddyyyy"},
                                {"seq": 2, "name": "yyyy-dd-MM", "value": "yyyyddmm"} ]
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703206",
                    "type": "select",
                    "common": {"mapping-id": ""},
                    "label": {
                        "position": "left", "column": 2, "size": 12, "color": "#ff3399", "bold": "N", "italic": "N", 
                        "underline": "N", "text": "언어", "align": "left"},
                    "display": {"column": 10, "order": 6},
                    "validate": {"required": "Y"},
                    "option": [ {"seq": 2, "name": "한국어", "value": "ko"},
                                {"seq": 1, "name": "영어", "value": "en"} ]
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703207",
                    "type": "radio",
                    "common": {"mapping-id": ""},
                    "label": {
                        "position": "top", "column": 2, "size": 12, "color": "#000000", "bold": "Y", "italic": "N", 
                        "underline": "N", "text": "라디오 버튼", "align": "left"},
                    "display": {"column": 10, "direction": "horizontal", "position": "left","order": 7},
                    "validate": {"required": "Y"},
                    "option": [ {"seq": 2, "name": "radio button2", "value": "radio2"},
                                {"seq": 1, "name": "radio button1", "value": "radio1"} ]
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703208",
                    "type": "checkbox",
                    "common": {"mapping-id": ""},
                    "label": {
                        "position": "left", "column": 2, "size": 15, "color": "#ff0000", "bold": "Y", "italic": "N", 
                        "underline": "N", "text": "Check Box", "align": "left"},
                    "display": {"column": 10, "direction": "vertical", "position": "right","order": 8},
                    "validate": {"required": "Y"},
                    "option": [{"seq": 2, "name": "제목", "value": "title"},
                               {"seq": 1, "name": "작성자", "value": "name"}]
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703208",
                    "type": "image",
                    "common": {"mapping-id": ""},
                    "display": {
                        "path": "https://placehold.it/700X50", "width": 700, "height": 50, "align": "center", 
                        "order": 8}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703209",
                    "type": "date",
                    "common": {"mapping-id": ""},
                    "label": {"position": "left", "column": 5, "size": 16, "color": "#33ccff", "bold": "N",
                    "italic": "Y", "underline": "Y", "text": "날짜(yyyy-MM-dd)", "align": "center"},
                    "display": {"column": 7, "format": "yyyy-MM-dd", "default": "today", "order": 9},
                    "validate": {"required": "Y", "date-min": "2020-01-31", "date-max": "2020-03-01"}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d37032010",
                    "type": "time",
                    "common": {"mapping-id": ""},
                    "label": {"position": "left", "column": 5, "size": 16, "color": "#ff3399", "bold": "Y",
                    "italic": "N", "underline": "N", "text": "시간(HH:mm)", "align": "center"},
                    "display": {"column": 7, "format": "HH:mm", "default": "", "order": 10},
                    "validate": {"required": "Y"}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703211",
                    "type": "datetime",
                    "common": {"mapping-id": ""},
                    "label": {"position": "left", "column": 5, "size": 16, "color": "#009933", "bold": "Y",
                    "italic": "N", "underline": "Y", "text": "날짜시간(yyyy-MM-dd HH:mm)", "align": "center"},
                    "display": {"column": 7, "format": "yyyy-MM-dd HH:mm", "default": "now", "order": 11},
                    "validate": {"required": "Y", "date-min": "", "date-max": ""}
                },
                {
                    "id": "a3f7b7f9-c7de-4ffc-a819-0be1d3703212",
                    "type": "fileupload",
                    "common": {"mapping-id": ""},
                    "display": {"order": 12}
                }]
                }
                """
    }

    /**
     * 문서 신규 등록 / 처리
     */
    @PostMapping("/data")
    fun createDocument(@RequestBody tokenSaveDto: TokenSaveDto): Boolean {
        return documentService.createDocument(tokenSaveDto)
    }

    /**
     * 문서 수정 / 처리
     */
    @PutMapping("/data")
    fun saveDocument(@RequestBody tokenSaveDto: TokenSaveDto): Boolean {
        return documentService.saveDocument(tokenSaveDto)
    }
}