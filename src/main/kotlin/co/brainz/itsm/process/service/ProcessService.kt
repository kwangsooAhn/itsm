/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.process.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.process.dto.SimulationReportDto
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
    fun createProcess(
        jsonData: Any
        //restTemplateProcessDto: RestTemplateProcessDto
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val restTemplateProcessDto = mapper.convertValue(jsonData, RestTemplateProcessDto::class.java)
        restTemplateProcessDto.createUserKey = currentSessionUser.getUserKey()
        restTemplateProcessDto.createDt = LocalDateTime.now()
        restTemplateProcessDto.processStatus = WorkflowConstants.ProcessStatus.EDIT.value
        val duplicateCount = wfProcessRepository.countByProcessName(restTemplateProcessDto.processName)
        val resultMap = mutableMapOf("processId" to "")
        if (duplicateCount > 0) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            //resultMap["result"] = WfProcessConstants.ResultCode.DUPLICATE.code
            //return mapper.writeValueAsString(resultMap)
        } else {
            resultMap["processId"] = wfProcessService.insertProcess(restTemplateProcessDto).processId
            //resultMap["result"] = WfProcessConstants.ResultCode.SUCCESS.code
        }
        return ZResponse(
            status = status.code,
            data = resultMap
        )
        //return mapper.writeValueAsString(resultMap)
    }

    /**
     * 프로세스 업데이트
     */
    fun updateProcessData(
        processId: String,
        restTemplateProcessElementDto: RestTemplateProcessElementDto
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        restTemplateProcessElementDto.process?.updateDt = LocalDateTime.now()
        restTemplateProcessElementDto.process?.updateUserKey = currentSessionUser.getUserKey()
        val duplicateCount = wfProcessRepository.countByProcessName(restTemplateProcessElementDto.process!!.name!!)
        val preRestTemplateProcessDto = wfProcessRepository.findByProcessId(processId)
        if (duplicateCount > 0 &&
            (preRestTemplateProcessDto!!.processName != restTemplateProcessElementDto.process!!.name)
        ) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            wfProcessService.updateProcessData(restTemplateProcessElementDto)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 프로세스 다른 이름 저장.
     */
    fun saveAsProcess(
        jsonData: Any
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val restTemplateProcessElementDto = mapper.convertValue(jsonData, RestTemplateProcessElementDto::class.java)
        // TODO: processId를 data 안에 포함한다. 아래 구조와 같아야 함
        // { status: '', message: '', data: { processId: ''} }
        restTemplateProcessElementDto.process?.createDt = LocalDateTime.now()
        restTemplateProcessElementDto.process?.createUserKey = currentSessionUser.getUserKey()
        restTemplateProcessElementDto.process?.updateDt = null
        restTemplateProcessElementDto.process?.updateUserKey = null
        restTemplateProcessElementDto.process?.status = WorkflowConstants.ProcessStatus.EDIT.value
        val duplicateCount = wfProcessRepository.countByProcessName(restTemplateProcessElementDto.process!!.name!!)
        val resultMap = mutableMapOf("processId" to "")
        if (duplicateCount > 0) {
            //resultMap["result"] = WfProcessConstants.ResultCode.DUPLICATE.code
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            //return mapper.writeValueAsString(resultMap)
        } else {
            resultMap["processId"] = wfProcessService.saveAsProcess(restTemplateProcessElementDto).processId
        }
        //resultMap["result"] = WfProcessConstants.ResultCode.SUCCESS.code
        //return mapper.writeValueAsString(resultMap)

        return ZResponse(
            status = status.code,
            data = resultMap
        )
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): ZResponse {
        return wfProcessService.deleteProcess(processId)
    }

    /**
     * 프로세스 시뮬레이션
     */
    fun getProcessSimulation(processId: String): SimulationReportDto {
        return wfProcessService.getProcessSimulation(processId)
    }
}
