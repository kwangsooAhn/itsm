package co.brainz.itsm.process.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.process.dto.ProcessStatusDto
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.ZoneId
import javax.xml.parsers.DocumentBuilderFactory
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

@Service
@Transactional
class ProcessService(
    private val restTemplate: RestTemplateProvider,
    private val aliceFileService: AliceFileService
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
        restTemplateProcessDto.createDt = LocalDateTime.now(ZoneId.of("UTC"))
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
        restTemplateProcessElementDto.process?.updateDt = LocalDateTime.now(ZoneId.of("UTC"))
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
        restTemplateProcessElementDto.process?.createDt = LocalDateTime.now(ZoneId.of("UTC"))
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

    /**
     * 프로세스 상태.
     */
    fun getProcessStatus(instanceId: String): ProcessStatusDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Instance.GET_INSTANCE_LATEST.url.replace(
                restTemplate.getKeyRegex(),
                instanceId
            )
        )
        val resultString = restTemplate.get(url)
        val processStatusDto = Gson().fromJson(resultString, ProcessStatusDto::class.java)
        val xmlFile = aliceFileService.getProcessStatusFile(processStatusDto.processId)
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
