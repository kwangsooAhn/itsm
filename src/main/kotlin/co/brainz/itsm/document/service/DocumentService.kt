package co.brainz.itsm.document.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.itsm.provider.dto.RestTemplateFormDto
import co.brainz.workflow.engine.process.dto.WfProcessDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
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
     * 가용한 문서양식 리스트 조회.
     *
     * @return List<RestTemplateFormDto>
     */
    fun findFormList(key: String, keyValue: String?): List<RestTemplateFormDto> {
        var params = LinkedMultiValueMap<String, String>()
        params.add(key, RestTemplateConstants.FormStatus.PUBLISH.value)
        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.GET_FORMS.url, parameters = params)
        val responseBody = restTemplate.get(urlDto)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val forms: List<RestTemplateFormDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateFormDto::class.java))
        for (form in forms) {
            form.createDt = form.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
            form.updateDt = form.updateDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }
        System.out.print("test :" + forms.size)
        return forms
    }

    /**
     * 가용한 프로세스 리스트 조회.
     *
     * @return List<RestTemplateProcessDto>
     */
    fun findProcessList(key: String): List<WfProcessDto> {
        var params = LinkedMultiValueMap<String, String>()
        params.add("status", RestTemplateConstants.ProcessStatus.PUBLISH.value)
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.GET_PROCESSES.url, parameters = params)
        val responseBody = restTemplate.get(url)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val processes: List<WfProcessDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, WfProcessDto::class.java))
        for (process in processes) {
            process.createDt = process.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
            process.updateDt = process.updateDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return processes
    }

    /**
     * 신청서 생성
     *
     * @return RestTemplateDocumentDto
     */
    fun createDocument(restTemplateDocumentDto: RestTemplateDocumentDto): String? {
        // TODO (form_id, process_id) 조합 중복체크 : 해당 일감은 추후 진행합니다.
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateDocumentDto.createUserKey = aliceUserDto.userKey
        restTemplateDocumentDto.createDt =  AliceTimezoneUtils().toGMT(LocalDateTime.now())
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.POST_DOCUMENT.url)
        val responseBody: String = restTemplate.create(url, restTemplateDocumentDto)
        return when (responseBody.isNotEmpty()) {
            true -> {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                val dataDto = mapper.readValue(responseBody, restTemplateDocumentDto::class.java)
                dataDto.documentId
            }
            false -> ""
        }
    }

    /**
     * 신청서 삭제
     *
     * @return Boolean
     */
    fun deleteDocument(documentId: String): Boolean {
        // TODO wfEngene에서 삭제 가능 여부 체크 : 해당 일감은 추후 진행합니다.
        return false
    }
}