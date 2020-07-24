package co.brainz.itsm.process.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import java.time.LocalDateTime

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
    fun getProcessAdmin(processId: String): RestTemplateProcessViewDto {
        val url = RestTemplateUrlDto(
            //callUrl = RestTemplateConstants.Process.GET_PROCESS_DATA.url.replace(
            callUrl = RestTemplateConstants.Process.GET_PROCESS.url.replace(
                restTemplate.getKeyRegex(),
                processId
            )
        )

        return mapper.readValue(
            restTemplate.get(url),
            mapper.typeFactory.constructType(RestTemplateProcessViewDto::class.java)
        )
    }

    /**
     * [processId], [restTemplateProcessDto]를 받아서 프로세스 마스터 데이터 업데이트.
     */
    fun updateProcess(processId: String, restTemplateProcessDto: RestTemplateProcessDto): Boolean {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessDto.updateDt = LocalDateTime.now()
        restTemplateProcessDto.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Process.PUT_PROCESS.url.replace(
                restTemplate.getKeyRegex(),
                processId
            )
        )
        val responseEntity = restTemplate.update(url, restTemplateProcessDto)
        return responseEntity.body.toString().isNotEmpty()
    }

}
