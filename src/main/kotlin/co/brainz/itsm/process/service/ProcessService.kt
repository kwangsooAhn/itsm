package co.brainz.itsm.process.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.engine.process.dto.WfProcessDto
import co.brainz.workflow.engine.process.dto.WfProcessElementDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
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
class ProcessService(private val restTemplate: RestTemplateProvider) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 데이터 목록 조회.
     */
    fun getProcesses(params: LinkedMultiValueMap<String, String>): List<WfProcessDto> {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.GET_PROCESSES.url, parameters = params)
        val responseBody = restTemplate.get(url)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val wfProcessList: List<WfProcessDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, WfProcessDto::class.java))
        for (item in wfProcessList) {
            item.createDt = item.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
            item.updateDt = item.updateDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return wfProcessList
    }

    /**
     * 프로세스 데이터 조회.
     */
    fun getProcess(processId: String): String {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.GET_PROCESS.url.replace(restTemplate.getKeyRegex(), processId))
        return restTemplate.get(url)
    }

    /**
     * 프로세스 신규 등록
     */
    fun createProcess(restTemplateProcessDto: RestTemplateProcessDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessDto.createUserKey = aliceUserDto.userKey
        restTemplateProcessDto.createDt =  AliceTimezoneUtils().toGMT(LocalDateTime.now())
        restTemplateProcessDto.processStatus = RestTemplateConstants.ProcessStatus.EDIT.code
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.POST_PROCESS.url)
        val responseBody = restTemplate.create(url, restTemplateProcessDto)
        return when (responseBody.isNotEmpty()) {
            true -> {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                val dataDto = mapper.readValue(responseBody, RestTemplateProcessDto::class.java)
                dataDto.processId
            }
            false -> ""
        }
    }

    /**
     * 프로세스 업데이트
     */
    fun updateProcess(wfProcessElementDto: WfProcessElementDto): Boolean {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val processId = wfProcessElementDto.process?.id?:""
        wfProcessElementDto.process?.updateDt = AliceTimezoneUtils().toGMT(LocalDateTime.now())
        wfProcessElementDto.process?.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.PUT_PROCESS.url.replace(restTemplate.getKeyRegex(), processId))
        return restTemplate.update(url, wfProcessElementDto)
    }

    /**
     * 프로세스 다른 이름 저장.
     */
    fun saveAsProcess(wfProcessElementDto: WfProcessElementDto): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        wfProcessElementDto.process?.createDt = AliceTimezoneUtils().toGMT(LocalDateTime.now())
        wfProcessElementDto.process?.createUserKey = userDetails.userKey
        wfProcessElementDto.process?.updateDt = null
        wfProcessElementDto.process?.updateUserKey = null
        if (wfProcessElementDto.process?.status == RestTemplateConstants.ProcessStatus.DESTROY.code) {
            wfProcessElementDto.process?.status = RestTemplateConstants.ProcessStatus.EDIT.code
        }
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.POST_PROCESS_SAVE_AS.url)
        val responseBody = restTemplate.createToSave(url, wfProcessElementDto)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return when (responseBody.isNotEmpty()) {
            true -> {
                val processDto = mapper.readValue(responseBody, RestTemplateProcessDto::class.java)
                processDto.processId
            }
            false -> ""
        }
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): Boolean {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.DELETE_PROCESS.url.replace(restTemplate.getKeyRegex(), processId))
        return  restTemplate.delete(url)
    }
}
