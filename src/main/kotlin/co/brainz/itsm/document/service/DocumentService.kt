package co.brainz.itsm.document.service

import co.brainz.itsm.provider.ProviderDocument
import co.brainz.itsm.provider.dto.DocumentDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class DocumentService(private val providerDocument: ProviderDocument) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<DocumentDto>
     */
    fun findDocumentList(): List<DocumentDto> {
        val responseBody = providerDocument.getDocuments()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        return mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, DocumentDto::class.java))
    }
}
