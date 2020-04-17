package co.brainz.itsm.document.controller

import co.brainz.itsm.document.service.DocumentService
import co.brainz.workflow.provider.dto.RestTemplateDocumentDataDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

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
        return documentService.findDocument(documentId)
    }

    /**
     * 신청서 생성
     *
     * @param restTemplateDocumentDto
     * */
    @PostMapping("")
    fun createDocument(@RequestBody restTemplateDocumentDto: RestTemplateDocumentDto): String? {
        return documentService.createDocument(restTemplateDocumentDto)
    }

    /**
     * 신청서 삭제
     *
     * @param documentId
     * */
    @DeleteMapping("/{documentId}")
    fun deleteDocument(@PathVariable documentId: String): ResponseEntity<String> {
        return documentService.deleteDocument(documentId)
    }

    /**
     * 신청서 목록 조회.
     */
    @GetMapping("/", "")
    fun getDocuments(): List<RestTemplateDocumentDto> {
        return documentService.findDocumentList()
    }

    /**
     * 신청서 편집 데이터 조회.
     */
    @GetMapping("/{documentId}/display")
    fun getDocumentDisplay(@PathVariable documentId: String): String {
//        return documentService.findDocumentDisplay(documentId)
        return dummyData(documentId)
    }

    fun dummyData(documentId: String): String {
        return """
            {
            "documentId":"32ds1261420w7edbcd5251d7b24a6c23",                                    
            "elements":[
                {"elementId" : "40288ab770be838a0170be84789b0000",
                 "attributeValue" : "userTask1"},              
                {"elementId" : "40288ab770be838a0170be84789b0001",
                 "attributeValue" : "userTask2"}],
            "components" : [
                {"componentId" : "40288ab0709f5ff301709f61a5a30003",
                 "attributeValue" : "TEXT",
                 "displayValue" : [
                    {"elementId" : "40288ab770be838a0170be84789b0000",
                     "display" : "editableRequired"},             
                    {"elementId" : "40288ab770be838a0170be84789b0001",
                     "display" : "readonly"}]
                },
                {"componentId" : "40288ab0709f5ff301709f61a5a30004",
                 "attributeValue" : "TEXT2",
                 "displayValue" : [
                    {"elementId" : "40288ab770be838a0170be84789b0000",
                     "display" : "hidden"},
                    {"elementId" : "40288ab770be838a0170be84789b0001",
                     "display" : "editable"}]
                }]                                    
            }                                        
        """
    }

    /**
     * 신청서 편집 데이터 저장.
     */
    @PutMapping("/{documentId}/display")
    fun updateDocumentDisplay(@PathVariable documentId: String, @RequestBody restTemplateDocumentDataDto: RestTemplateDocumentDataDto): String {
        // 수정
        var test = documentId;
        var test1 = restTemplateDocumentDataDto;
        System.out.println("test1:" + test1);

        return "true";
    }
}
