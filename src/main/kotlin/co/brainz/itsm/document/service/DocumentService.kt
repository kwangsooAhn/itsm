package co.brainz.itsm.document.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.process.service.ProcessService
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
import java.time.ZoneId
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Service
class DocumentService(
    private val restTemplate: RestTemplateProvider,
    private val formService: FormService,
    private val processService: ProcessService
) {

    private val objMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 신청서 리스트 조회.
     *
     * @return List<DocumentDto>
     */
    fun getDocumentList(restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto): List<RestTemplateDocumentDto> {
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

        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateDocumentDto::class.java)
        )
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
        restTemplateDocumentDto.createDt = LocalDateTime.now(ZoneId.of("UTC"))
        // TODO: 최초 생성시 상태 값은 임시로 변경해야 한다. (추후 작업)
        restTemplateDocumentDto.documentStatus = RestTemplateConstants.DocumentStatus.USE.value
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
    fun updateDocument(restTemplateDocumentDto: RestTemplateDocumentDto): String? {
        val documentId = restTemplateDocumentDto.documentId
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateDocumentDto.updateUserKey = aliceUserDto.userKey
        restTemplateDocumentDto.updateDt = LocalDateTime.now(ZoneId.of("UTC"))
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Workflow.PUT_DOCUMENT.url.replace(
                restTemplate.getKeyRegex(),
                documentId
            )
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
        return formService.findForms(formParams)
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
        return processService.getProcesses(processParams)
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
