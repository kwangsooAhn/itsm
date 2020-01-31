package co.brainz.itsm.process.service

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
class ProcessService(private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${server.protocol}")
    lateinit var protocol: String

    @Value("\${server.port}")
    lateinit var port: String

    fun makeUri(callUrl: String, params: MultiValueMap<String, String>): URI {
        val formUrl = protocol + "://" + InetAddress.getLocalHost().hostAddress + ":" + port + callUrl
        val uri = UriComponentsBuilder.fromHttpUrl(formUrl)
        if (params.isNotEmpty()) {
            uri.queryParams(params)
        }

        return uri.build().toUri()
    }

    /**
     * 프로세스 데이터 조회.
     */
    fun selectProcessList(search: String): List<ProcessDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val uri = makeUri("/rest/processes", params)

        val procListJson = restTemplate.getForObject(uri, String::class.java)

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        return mapper.readValue(
            procListJson,
            mapper.typeFactory.constructCollectionType(List::class.java, ProcessDto::class.java)
        )
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
