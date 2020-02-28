package co.brainz.itsm.document.service

import co.brainz.itsm.provider.ProviderDocument
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.TokenProvider
import co.brainz.itsm.provider.dto.DocumentDto
import co.brainz.itsm.provider.dto.TokenSaveDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class DocumentService(private val providerDocument: ProviderDocument, private val tokenProvider: TokenProvider) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<DocumentDto>
     */
    fun findDocumentList(): List<DocumentDto> {
        val responseBody = providerDocument.getDocuments()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        val documents: List<DocumentDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, DocumentDto::class.java))
        for (document in documents) {
            document.createDt = document.createDt?.let { ProviderUtilities().toTimezone(it) }
            document.updateDt = document.updateDt?.let { ProviderUtilities().toTimezone(it) }
        }
        return documents
    }

    /**
     * 신청서 문서 데이터 조회.
     *
     * @return String
     */
    fun findDocument(documentId: String): String {
        return providerDocument.getDocument(documentId)
    }

    /**
     * 문서 신규 등록 / 처리
     * isComplete : false일경우에는 저장, true일경우에 처리
     *
     * @return : Boolean
     */
    fun createDocument(tokenSaveDto: TokenSaveDto): Boolean {
        return tokenProvider.postToken(tokenSaveDto)
    }

    /**
     * 문서 수정 / 처리
     * isComplete : false일경우에는 수정, true일경우에 처리
     *
     * @return Boolean
     */
    fun updateDocument(tokenSaveDto: TokenSaveDto): Boolean {
        return tokenProvider.putToken(tokenSaveDto)
    }
}
