package co.brainz.itsm.process.service

import co.brainz.itsm.provider.ProviderProcess
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.dto.FormDto
import co.brainz.workflow.process.ProcessConstants
import co.brainz.workflow.process.ProcessDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.InetAddress
import java.net.URI

@Service
class ProcessService(private val providerProcess: ProviderProcess) {

    private val logger = LoggerFactory.getLogger(this::class.java)

/*    @Value("\${server.protocol}")
    lateinit var protocol: String

    @Value("\${server.port}")
    lateinit var port: String

    private val uri = "/rest/wf/processes"*/

/*    fun makeUri(callUrl: String, params: MultiValueMap<String, String>): URI {
        val formUrl = protocol + "://" + InetAddress.getLocalHost().hostAddress + ":" + port + callUrl
        val uri = UriComponentsBuilder.fromHttpUrl(formUrl)
        if (params.isNotEmpty()) {
            uri.queryParams(params)
        }

        return uri.build().toUri()
    }*/

    /**
     * 프로세스 데이터 조회.
     */
    fun findProcessList(search: String): List<ProcessDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val responseBody = providerProcess.getProcesses(params)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val processList: List<ProcessDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, ProcessDto::class.java))
        for (item in processList) {
            item.createDt = item.createDt?.let { ProviderUtilities().toTimezone(it) }
            item.updateDt = item.updateDt?.let { ProviderUtilities().toTimezone(it) }
        }

        return processList
    }

    /**
     * 프로세스 신규 기본 정보 등록.
     */
    fun insertProcess(processDto: ProcessDto): String {
        //TODO DB에 저장.
        val userName: String = SecurityContextHolder.getContext().authentication.name //사용자 이름
        val status = ProcessConstants.Status.EDIT.code // 등록 시 프로세스 상태

        //등록된 process_id return
        return "test8cbdd784401aaad6d310df85ac2d"
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String) {
        //TODO DB에서 삭제.
    }
}
