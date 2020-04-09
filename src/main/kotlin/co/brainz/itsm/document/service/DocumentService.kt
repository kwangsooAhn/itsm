package co.brainz.itsm.document.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

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

    /**
     * 신청서 생성.
     *
     * @param restTemplateDocumentDto
     * @return String?
     */
    fun createDocument(restTemplateDocumentDto: RestTemplateDocumentDto): String? {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateDocumentDto.createUserKey = aliceUserDto.userKey
        restTemplateDocumentDto.createDt =  AliceTimezoneUtils().toGMT(LocalDateTime.now())
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.POST_DOCUMENT.url)
        val responseBody = restTemplate.create(url, restTemplateDocumentDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                val dataDto = mapper.readValue(responseBody.body.toString(), restTemplateDocumentDto::class.java)
//                // 문서번호를 통해 양식 데이터를 초기화한다.
//                createDocumentData(dataDto)
                dataDto.documentId
            }
            false -> ""
        }
    }

    /**
     * 신청서 삭제.
     *
     * @param documentId
     * @return Boolean
     */
    fun deleteDocument(documentId: String): ResponseEntity<String> {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.DELETE_DOCUMENT.url.replace(restTemplate.getKeyRegex(), documentId))
        return restTemplate.delete(url)
    }

    /**
     * 신청서 display 생성. (기본값)
     *
     * @param restTemplateDocumentDto
     * @return String?
     */
    fun createDocumentData(restTemplateDocumentDto: RestTemplateDocumentDto): String? {
        val documentId = restTemplateDocumentDto.documentId.toString()
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.POST_DOCUMENTS_DISPLAY.url.replace(restTemplate.getKeyRegex(), documentId))
        val responseBody = restTemplate.create(url, restTemplateDocumentDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> "true"
            false -> "false"
        }
    }

    /**
     * 신청서 display 조회
     *
     * @return List<DocumentDto>
     */
    fun findDocumentDisplay(documentId: String): String? {//List<RestTemplateDocumentDto> {
        return ""
    }
}
