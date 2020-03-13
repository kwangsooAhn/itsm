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
class ProcessService(private val restTemplateProvider: RestTemplateProvider) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 데이터 목록 조회.
     */
    fun getProcesses(search: String): List<WfProcessDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.GET_PROCESSES.url, parameters = params)
        val responseBody = restTemplateProvider.get(url)
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
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.GET_PROCESSES.url.replace(restTemplateProvider.getKeyRegex(), processId))
        return restTemplateProvider.get(url)
    }

    /**
     * 프로세스 신규 등록
     */
    fun createProcess(restTemplateProcessDto: RestTemplateProcessDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessDto.createUserKey = aliceUserDto.userKey
        restTemplateProcessDto.createDt =  AliceTimezoneUtils().toGMT(LocalDateTime.now())
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.POST_PROCESS.url)
        val responseBody = restTemplateProvider.create(url, restTemplateProcessDto)
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
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.PUT_PROCESS.url.replace(restTemplateProvider.getKeyRegex(), processId))
        return restTemplateProvider.update(url, wfProcessElementDto)
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): Boolean {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Process.DELETE_PROCESS.url.replace(restTemplateProvider.getKeyRegex(), processId))
        return  restTemplateProvider.delete(url)
    }
}
