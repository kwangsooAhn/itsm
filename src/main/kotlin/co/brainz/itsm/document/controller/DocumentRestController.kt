package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/documents")
class DocumentRestController(private val documentService: DocumentService) {

    /**
     * 신청서의 문서 데이터 조회.
     *
     * @param documentId
     * */
    @GetMapping("/data/{documentId}")
    fun getDocument(@PathVariable documentId: String): String {
        if (documentId == "1111") {
            return dummyData(documentId)
        } else {
            return documentService.findDocument(documentId)
        }
    }

    fun dummyData(documentId: String): String {
        return """
            {
            "form":{"id":"40288ab770eb4bb90170eb5b645b0002","name":"김성민테스트","desc":"김성민테스트_20200318","status":"form.status.edit"},
            "components":[
              {"componentId":"aed521635c9114ba06c2272ba211add6",
               "attributes":{"type":"textarea","common":{"mapping-id":""},
               "display":{"rows":"3","placeholder":"","column":"10","outline-width":"1","outline-color":"#000000","order":1},
               "label":{"position":"left","column":"2","size":"12","color":"#000000","bold":"N","italic":"N","underline":"N","align":"left","text":"요청자"},
               "validate":{"required":"Y","length-min":"0","length-max":"30"}},
               "values":[{"value":""}]},
              {"componentId":"ac2f303b34ba081a03935e9d8e85fe56",
               "attributes":{"type":"select","common":{"mapping-id":""},
               "display":{"column":"10","order":2},
               "label":{"position":"left","column":"2","size":"12","color":"#000000","bold":"N","italic":"N","underline":"N","align":"left","text":"접수방법"},
               "option":[{"seq":"1","name":"이메일","value":"test1"},{"seq":2,"name":"전화","value":"test2"}],
               "validate":{"required":"N"}},
               "values":[{"value":""}]},
              {"componentId":"a8c2f17d08a84b76320b9aafa7247349",
               "attributes":{"type":"checkbox","common":{"mapping-id":""},
               "display":{"column":"10","direction":"horizontal","position":"right","order":3},
               "label":{"position":"left","column":"2","size":"12","color":"#000000","bold":"N","italic":"N","underline":"N","align":"left","text":"프로세스"},
               "option":[{"seq":"1","name":"서비스","value":"sd"},{"seq":2,"name":"장애","value":"inc"},{"seq":3,"name":"변경","value":"chg"}],
               "validate":{"required":"N"}},
               "values":[{"value":""}]},
               {"componentId":"ac7caa2174d0f5231a6ea3e1c5518786",
                "attributes":{"type":"text","common":{"mapping-id":""},
                "display":{"placeholder":"","column":"10","default":"none|","outline-width":"1","outline-color":"#000000","order":4},
                "label":{"position":"left","column":"2","size":"12","color":"#000000","bold":"N","italic":"N","underline":"N","align":"left","text":"제목"},
                "validate":{"required":"N","regexp":"none","regexp-msg":"","length-min":"0","length-max":"30"}},
                "values":[{"value":""}]},
               {"componentId":"ae4c4a34c07c7bb9d6c6f5c04bd2f4aa",
                "attributes":{"type":"radio","common":{"mapping-id":""},"display":{"column":"10","direction":"horizontal","position":"right","order":5},
                "label":{"position":"left","column":"2","size":"12","color":"#000000","bold":"N","italic":"N","underline":"N","align":"left","text":"우선순위"},
                "option":[{"seq":"1","name":"긴급","value":"1"},{"seq":2,"name":"보통","value":"2"},
                {"seq":3,"name":"낮음","value":"3"}],
                "validate":{"required":"N"}},
                "values":[{"value":""}]
                },
                {"componentId":"a3f2f42ce627bf2464df0d833b155130",
                  "attributes":{"type":"date","common":{"mapping-id":""},
                  "display":{"column":"10","default":"datepicker|19-03-2020","order":1},
                  "label":{"position":"left","column":"2","size":"12","color":"#000000","bold":"N","italic":"N","underline":"N","align":"left","text":"TEXT"},
                  "validate":{"required":"N","date-min":"1900-01-01","date-max":"2999-12-31"}},"values":[{"value":""}]},
                 {"componentId":"a965f98cf3e6a16565e5b8d3770ccd7a","attributes":{"type":"fileupload","common":{"mapping-id":""},
                  "display":{"column":"10","order":2},
                  "label":{"position":"left","column":"2","size":"12","color":"#000000","bold":"N","italic":"N","underline":"N","align":"left","text":"TEXT"},
                  "validate":{"required":"N"}},"values":[{"value":""}]}
            ]
            ,"action":[{"name" : "등록","value" : "" }]
            }
        """
    }
}