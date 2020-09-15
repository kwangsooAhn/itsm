package co.brainz.itsm.document.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.form.service.FormAdminService
import co.brainz.itsm.process.service.ProcessAdminService
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Service
class DocumentService(
    private val restTemplate: RestTemplateProvider,
    private val formAdminService: FormAdminService,
    private val processAdminService: ProcessAdminService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 신청서 리스트 조회.
     *
     * @return List<DocumentDto>
     */
    fun getDocumentList(
        restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto
    ): List<RestTemplateDocumentDto> {
        val multiVal: MultiValueMap<String, String> = LinkedMultiValueMap()
        multiVal.setAll(
            objMapper.convertValue<Map<String, String>>(
                restTemplateDocumentSearchListDto,
                object : TypeReference<Map<String, String>>() {}
            )
        )

        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_DOCUMENTS.url, parameters = multiVal)
        val responseBody = restTemplate.get(url) // providerDocument.getDocuments()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val documentList: List<RestTemplateDocumentDto> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateDocumentDto::class.java)
        )
        for (document in documentList) {
            if (document.documentIcon.isNullOrEmpty()) {
                document.documentIcon = DocumentConstants.DEFAULT_DOCUMENT_ICON
            }
        }
        return documentList
    }

    /**
     * 신청서 조회.
     */
    fun getDocument(documentId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Workflow.GET_DOCUMENT.url.replace(
                restTemplate.getKeyRegex(),
                documentId
            )
        )
        return restTemplate.get(url)
    }

    /**
     * 신청서 문서 데이터 조회.
     *
     * @return String
     */
    fun getDocumentData(documentId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Workflow.GET_DOCUMENT_DATA.url.replace(
                restTemplate.getKeyRegex(),
                documentId
            )
        )
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
        restTemplateDocumentDto.createDt = LocalDateTime.now()
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.POST_DOCUMENT.url)
        val responseBody = restTemplate.create(url, restTemplateDocumentDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                val dataDto = mapper.readValue(responseBody.body.toString(), restTemplateDocumentDto::class.java)
                dataDto.documentId
            }
            false -> ""
        }
    }

    /**
     * Update Document.
     *
     * @param restTemplateDocumentDto
     * @return Boolean
     */
    fun updateDocument(
        restTemplateDocumentDto: RestTemplateDocumentDto,
        params: LinkedMultiValueMap<String, String>
    ): String? {
        val documentId = restTemplateDocumentDto.documentId
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateDocumentDto.updateUserKey = aliceUserDto.userKey
        restTemplateDocumentDto.updateDt = LocalDateTime.now()
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Workflow.PUT_DOCUMENT.url.replace(
                restTemplate.getKeyRegex(),
                documentId
            ),
            parameters = params
        )
        val responseEntity = restTemplate.update(url, restTemplateDocumentDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                documentId
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
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Workflow.DELETE_DOCUMENT.url.replace(
                restTemplate.getKeyRegex(),
                documentId
            )
        )
        return restTemplate.delete(url)
    }

    /**
     * Get Form List.
     *
     * @return List<RestTemplateFormDto>
     */
    fun getFormList(): List<RestTemplateFormDto> {
        val formParams = LinkedMultiValueMap<String, String>()
        val formStatus = ArrayList<String>()
        formStatus.add(RestTemplateConstants.FormStatus.PUBLISH.value)
        formStatus.add(RestTemplateConstants.FormStatus.USE.value)
        formParams["status"] = formStatus.joinToString(",")
        return formAdminService.findForms(formParams)
    }

    /**
     * Get Process List.
     *
     * @return List<RestTemplateProcessViewDto>
     */
    fun getProcessList(): List<RestTemplateProcessViewDto> {
        val processParams = LinkedMultiValueMap<String, String>()
        val processStatus = ArrayList<String>()
        processStatus.add(RestTemplateConstants.ProcessStatus.PUBLISH.value)
        processStatus.add(RestTemplateConstants.ProcessStatus.USE.value)
        processParams["status"] = processStatus.joinToString(",")
        return processAdminService.getProcesses(processParams)
    }

    /**
     * 신청서 양식 데이터 조회
     *
     * @param documentId
     * @return List<DocumentDto>
     */
    fun getDocumentDisplay(documentId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Workflow.GET_DOCUMENTS_DISPLAY.url.replace(
                restTemplate.getKeyRegex(),
                documentId
            )
        )
        return restTemplate.get(url)
    }

    /**
     * 신청서 양식 데이터 update
     *
     * @param documentDisplay
     * @return Boolean
     */
    fun updateDocumentDisplay(documentDisplay: RestTemplateDocumentDisplayDto): Boolean {
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Workflow.PUT_DOCUMENTS_DISPLAY.url.replace(
                restTemplate.getKeyRegex(),
                documentDisplay.documentId.toString()
            )
        )
        val responseEntity = restTemplate.update(urlDto, documentDisplay)
        return responseEntity.body.toString().isNotEmpty()
    }
}
