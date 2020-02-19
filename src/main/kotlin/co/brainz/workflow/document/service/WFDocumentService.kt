package co.brainz.workflow.document.service

import co.brainz.workflow.document.dto.DocumentDto
import org.springframework.stereotype.Service

@Service
class WFDocumentService {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<DocumentDto>
     */
    fun documentList(): List<DocumentDto> {
        //TODO 프로세스에 매핑된 문서 중 상태가 발행인 문서를 조회한다.
        val documentList = mutableListOf<DocumentDto>()
        val document1 = DocumentDto(
                documentId = "fbf0e2c7175245df9260c7f94ff42bf6",
                documentName = "인프라 변경",
                documentDesc = "인프라 변경 관련 사항을 접수하는 문서양식"
        )
        val document2 = DocumentDto(
                documentId = "16cf1961700e4edbbf5251d7b84b3b99",
                documentName = "단순문의",
                documentDesc = "단순한 문의사항 접수하는 문서양식"
        )
        documentList.add(document1)
        documentList.add(document2)

        return documentList
    }

    /**
     * 신청서 1건 조회.
     *
     * @param documentId
     * @return DocumentDto
     */
    fun document(documentId: String): DocumentDto {
        //TODO 프로세스에 매핑된 문서 중 상태가 발행인 문서를 조회한다. 1건.
        return DocumentDto(
                documentId = "fbf0e2c7175245df9260c7f94ff42bf6",
                documentName = "인프라 변경",
                documentDesc = "인프라 변경 관련 사항을 접수하는 문서양식"
        )
    }
}
