package co.brainz.itsm.process.service

import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap

@Service
@Transactional
class ProcessAdminService(
    private val restTemplate: RestTemplateProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 프로세스 데이터 목록 조회.
     */
    fun getProcesses(params: LinkedMultiValueMap<String, String>): List<RestTemplateProcessViewDto> {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.GET_PROCESSES.url, parameters = params)
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateProcessViewDto::class.java)
        )
    }

    /**
     * [processId]를 받아서 프로세스 마스터 데이터 조회.
     */
    fun getProcessAdmin(processId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Process.GET_PROCESS_DATA.url.replace(
                restTemplate.getKeyRegex(),
                processId
            )
        )
        return restTemplate.get(url)
    }
}
