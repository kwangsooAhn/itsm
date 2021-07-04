/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.itsm.process.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.workflow.process.constants.WfProcessConstants
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.process.service.WfProcessService
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessListReturnDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProcessAdminService(
    private val wfProcessService: WfProcessService,
    private val wfProcessRepository: WfProcessRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 프로세스 데이터 목록 조회.
     */
    fun getProcesses(params: LinkedHashMap<String, Any>): RestTemplateProcessListReturnDto {
        return wfProcessService.getProcesses(params)
    }

    /**
     * [processId]를 받아서 프로세스 마스터 데이터 조회.
     */
    fun getProcessAdmin(processId: String): RestTemplateProcessViewDto {
        return wfProcessService.getProcessDetail(processId)
    }

    /**
     * [processId], [restTemplateProcessDto]를 받아서 프로세스 마스터 데이터 업데이트.
     */
    fun updateProcess(processId: String, restTemplateProcessDto: RestTemplateProcessDto): Int {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessDto.updateDt = LocalDateTime.now()
        restTemplateProcessDto.updateUserKey = userDetails.userKey
        val duplicateCount = wfProcessRepository.countByProcessName(restTemplateProcessDto.processName)
        val preRestTemplateProcessDto = wfProcessRepository.findByProcessId(processId)
        var result = WfProcessConstants.ResultCode.FAIL.code
        if (duplicateCount > 0 && (preRestTemplateProcessDto!!.processName != restTemplateProcessDto.processName)) {
            result = WfProcessConstants.ResultCode.DUPLICATE.code
            return result
        }
        if (wfProcessService.updateProcess(restTemplateProcessDto)) {
            result = WfProcessConstants.ResultCode.SUCCESS.code
        }
        return result
    }
}
