package co.brainz.itsm.document.service

import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class DocumentService(private val restTemplate: RestTemplateProvider) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<DocumentDto>
     */
    fun findDocumentList(): List<RestTemplateDocumentDto> {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_DOCUMENTS.url)
        val responseBody = restTemplate.get(url) //providerDocument.getDocuments()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        val restTemplateDocuments: List<RestTemplateDocumentDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateDocumentDto::class.java))
        for (document in restTemplateDocuments) {
            document.createDt = document.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
            document.updateDt = document.updateDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return restTemplateDocuments
    }

    /**
     * 신청서 문서 데이터 조회.
     *
     * @return String
     */
    fun findDocument(documentId: String): String {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_DOCUMENT.url.replace(restTemplate.getKeyRegex(), documentId))
        return restTemplate.get(url)
    }
}
