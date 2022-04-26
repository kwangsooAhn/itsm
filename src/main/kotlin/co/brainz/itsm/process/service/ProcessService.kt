/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.process.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.process.constants.WfProcessConstants
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.process.service.WfProcessService
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProcessService(
    private val wfProcessService: WfProcessService,
    private val wfProcessRepository: WfProcessRepository,
    private val currentSessionUser: CurrentSessionUser
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 프로세스 데이터 조회.
     */
    fun getProcessData(processId: String): RestTemplateProcessElementDto {
        return wfProcessService.getProcessData(processId)
    }

    /**
     * 프로세스 신규 등록
     */
    fun createProcess(restTemplateProcessDto: RestTemplateProcessDto): String {
        restTemplateProcessDto.createUserKey = currentSessionUser.getUserKey()
        restTemplateProcessDto.createDt = LocalDateTime.now()
        restTemplateProcessDto.processStatus = WorkflowConstants.ProcessStatus.EDIT.value
        val duplicateCount = wfProcessRepository.countByProcessName(restTemplateProcessDto.processName)
        val resultMap = mutableMapOf("processId" to "", "result" to WfProcessConstants.ResultCode.FAIL.code)
        if (duplicateCount > 0) {
            resultMap["result"] = WfProcessConstants.ResultCode.DUPLICATE.code
            return mapper.writeValueAsString(resultMap)
        }
        resultMap["processId"] = wfProcessService.insertProcess(restTemplateProcessDto).processId
        resultMap["result"] = WfProcessConstants.ResultCode.SUCCESS.code
        return mapper.writeValueAsString(resultMap)
    }

    /**
     * 프로세스 업데이트
     */
    fun updateProcessData(processId: String, restTemplateProcessElementDto: RestTemplateProcessElementDto): Int {
        restTemplateProcessElementDto.process?.updateDt = LocalDateTime.now()
        restTemplateProcessElementDto.process?.updateUserKey = currentSessionUser.getUserKey()
        val duplicateCount = wfProcessRepository.countByProcessName(restTemplateProcessElementDto.process!!.name!!)
        val preRestTemplateProcessDto = wfProcessRepository.findByProcessId(processId)
        var result = WfProcessConstants.ResultCode.FAIL.code
        if (duplicateCount > 0 &&
            (preRestTemplateProcessDto!!.processName != restTemplateProcessElementDto.process!!.name)
        ) {
            result = WfProcessConstants.ResultCode.DUPLICATE.code
            return result
        }
        if (wfProcessService.updateProcessData(restTemplateProcessElementDto)) {
            result = WfProcessConstants.ResultCode.SUCCESS.code
        }
        return result
    }

    /**
     * 프로세스 다른 이름 저장.
     */
    fun saveAsProcess(restTemplateProcessElementDto: RestTemplateProcessElementDto): String {
        // TODO: processId를 data 안에 포함한다. 아래 구조와 같아야 함
        // { status: '', message: '', data: { processId: ''} }
        restTemplateProcessElementDto.process?.createDt = LocalDateTime.now()
        restTemplateProcessElementDto.process?.createUserKey = currentSessionUser.getUserKey()
        restTemplateProcessElementDto.process?.updateDt = null
        restTemplateProcessElementDto.process?.updateUserKey = null
        restTemplateProcessElementDto.process?.status = WorkflowConstants.ProcessStatus.EDIT.value
        val duplicateCount = wfProcessRepository.countByProcessName(restTemplateProcessElementDto.process!!.name!!)
        val resultMap = mutableMapOf("processId" to "", "result" to WfProcessConstants.ResultCode.FAIL.code)
        if (duplicateCount > 0) {
            resultMap["result"] = WfProcessConstants.ResultCode.DUPLICATE.code
            return mapper.writeValueAsString(resultMap)
        }
        resultMap["processId"] = wfProcessService.saveAsProcess(restTemplateProcessElementDto).processId
        resultMap["result"] = WfProcessConstants.ResultCode.SUCCESS.code
        return mapper.writeValueAsString(resultMap)
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): Boolean {
        return wfProcessService.deleteProcess(processId)
    }

    /**
     * 프로세스 시뮬레이션
     */
    fun getProcessSimulation(processId: String): String {
        return mapper.writeValueAsString(wfProcessService.getProcessSimulation(processId))
    }
}
