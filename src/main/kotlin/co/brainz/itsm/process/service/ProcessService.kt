package co.brainz.itsm.process.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap

@Service
@Transactional
class ProcessService(private val restTemplate: RestTemplateProvider) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 프로세스 데이터 목록 조회.
     */
    fun getProcesses(params: LinkedMultiValueMap<String, String>): List<RestTemplateProcessViewDto> {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.GET_PROCESSES.url, parameters = params)
        val responseBody = restTemplate.get(url)
        val wfProcessList: List<RestTemplateProcessViewDto> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateProcessViewDto::class.java)
        )
        for (item in wfProcessList) {
            item.createDt = item.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
            item.updateDt = item.updateDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return wfProcessList
    }

    /**
     * 프로세스 데이터 조회.
     */
    fun getProcessData(processId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Process.GET_PROCESS_DATA.url.replace(
                restTemplate.getKeyRegex(),
                processId
            )
        )
        return restTemplate.get(url)
    }

    /**
     * 프로세스 신규 등록
     */
    fun createProcess(restTemplateProcessDto: RestTemplateProcessDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessDto.createUserKey = aliceUserDto.userKey
        restTemplateProcessDto.createDt = AliceTimezoneUtils().toGMT(LocalDateTime.now())
        restTemplateProcessDto.processStatus = RestTemplateConstants.ProcessStatus.EDIT.value
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.POST_PROCESS.url)
        val responseBody = restTemplate.create(url, restTemplateProcessDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> {
                val dataDto = mapper.readValue(responseBody.body.toString(), RestTemplateProcessDto::class.java)
                dataDto.processId
            }
            false -> ""
        }
    }

    /**
     * 프로세스 업데이트
     */
    fun updateProcessData(processId: String, restTemplateProcessElementDto: RestTemplateProcessElementDto): Boolean {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessElementDto.process?.updateDt = AliceTimezoneUtils().toGMT(LocalDateTime.now())
        restTemplateProcessElementDto.process?.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Process.PUT_PROCESS_DATA.url.replace(
                restTemplate.getKeyRegex(),
                processId
            )
        )
        val responseEntity = restTemplate.update(url, restTemplateProcessElementDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * 프로세스 다른 이름 저장.
     */
    fun saveAsProcess(restTemplateProcessElementDto: RestTemplateProcessElementDto): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessElementDto.process?.createDt = AliceTimezoneUtils().toGMT(LocalDateTime.now())
        restTemplateProcessElementDto.process?.createUserKey = userDetails.userKey
        restTemplateProcessElementDto.process?.updateDt = null
        restTemplateProcessElementDto.process?.updateUserKey = null
        restTemplateProcessElementDto.process?.status = RestTemplateConstants.ProcessStatus.EDIT.value
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.POST_PROCESS_SAVE_AS.url)
        val responseEntity = restTemplate.createToSave(url, restTemplateProcessElementDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                val processDto = mapper.readValue(responseEntity.body.toString(), RestTemplateProcessDto::class.java)
                processDto.processId
            }
            false -> ""
        }
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): ResponseEntity<String> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Process.DELETE_PROCESS.url.replace(
                restTemplate.getKeyRegex(),
                processId
            )
        )
        return restTemplate.delete(url)
    }

    /**
     * 프로세스 시뮬레이션
     */
    fun getProcessSimulation(processId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Process.GET_PROCESS_SIMULATION.url.replace(
                restTemplate.getKeyRegex(),
                processId
            )
        )
        return restTemplate.get(url)
    }
}
