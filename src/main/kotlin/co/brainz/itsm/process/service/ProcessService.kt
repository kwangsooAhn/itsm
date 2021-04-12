/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.process.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.fileTransaction.service.AliceFileProvider
import co.brainz.itsm.process.dto.ProcessStatusDto
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.process.constants.WfProcessConstants
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.process.service.WfProcessService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import java.time.LocalDateTime
import javax.xml.parsers.DocumentBuilderFactory
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

@Service
@Transactional
class ProcessService(
    private val aliceFileProvider: AliceFileProvider,
    private val wfInstanceService: WfInstanceService,
    private val wfProcessService: WfProcessService,
    private val wfProcessRepository: WfProcessRepository
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
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessDto.createUserKey = aliceUserDto.userKey
        restTemplateProcessDto.createDt = LocalDateTime.now()
        restTemplateProcessDto.processStatus = RestTemplateConstants.ProcessStatus.EDIT.value
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
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessElementDto.process?.updateDt = LocalDateTime.now()
        restTemplateProcessElementDto.process?.updateUserKey = userDetails.userKey
        val duplicateCount = wfProcessRepository.countByProcessName(restTemplateProcessElementDto.process!!.name!!)
        val preRestTemplateProcessDto = wfProcessRepository.findByProcessId(processId)
        var result = WfProcessConstants.ResultCode.FAIL.code
        if (duplicateCount > 0 && (preRestTemplateProcessDto!!.processName != restTemplateProcessElementDto.process!!.name)) {
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
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateProcessElementDto.process?.createDt = LocalDateTime.now()
        restTemplateProcessElementDto.process?.createUserKey = userDetails.userKey
        restTemplateProcessElementDto.process?.updateDt = null
        restTemplateProcessElementDto.process?.updateUserKey = null
        restTemplateProcessElementDto.process?.status = RestTemplateConstants.ProcessStatus.EDIT.value
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

    /**
     * 프로세스 상태.
     */
    fun getProcessStatus(instanceId: String): ProcessStatusDto {
        val resultString = Gson().toJson(wfInstanceService.getInstanceLatestToken(instanceId))
        val processStatusDto = Gson().fromJson(resultString, ProcessStatusDto::class.java)
        val xmlFile = aliceFileProvider.getProcessStatusFile(processStatusDto.processId)
        if (xmlFile.exists()) {
            val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile)
            xmlDoc.documentElement.normalize()

            val imageNodeList: NodeList = xmlDoc.getElementsByTagName("image")
            if (imageNodeList.length > 0) {
                val imageNode: Node = imageNodeList.item(0)
                if (imageNode.nodeType == Node.ELEMENT_NODE) {
                    val element = imageNode as Element
                    for (i in 0 until element.attributes.length) {
                        val nodeValue = element.attributes.item(i).nodeValue
                        when (element.attributes.item(i).nodeName) {
                            "left" -> processStatusDto.left = nodeValue
                            "top" -> processStatusDto.top = nodeValue
                            "width" -> processStatusDto.width = nodeValue
                            "height" -> processStatusDto.height = nodeValue
                        }
                    }
                    processStatusDto.imageData = element.textContent
                }

                val elementList = mutableListOf<LinkedHashMap<String, String>>()
                val elementNodeList: NodeList = xmlDoc.getElementsByTagName("element")
                for (i in 0 until elementNodeList.length) {
                    val elementNode: Node = elementNodeList.item(i)
                    if (elementNode.nodeType == Node.ELEMENT_NODE) {
                        val element = elementNode as Element
                        val elementMap = LinkedHashMap<String, String>()
                        for (j in 0 until element.attributes.length) {
                            elementMap[element.attributes.item(j).nodeName] = element.attributes.item(j).nodeValue
                        }
                        elementList.add(elementMap)
                    }
                }
                processStatusDto.elements = elementList
            }
        }
        return processStatusDto
    }
}
