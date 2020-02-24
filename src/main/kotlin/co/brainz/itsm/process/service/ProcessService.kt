package co.brainz.itsm.process.service

import co.brainz.itsm.provider.ProviderProcess
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.workflow.process.constants.ProcessConstants
import co.brainz.workflow.process.dto.WFProcessDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class ProcessService(private val providerProcess: ProviderProcess) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 데이터 조회.
     */
    fun findProcessList(search: String): List<WFProcessDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val responseBody = providerProcess.getProcesses(params)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val WFProcessList: List<WFProcessDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, WFProcessDto::class.java))
        for (item in WFProcessList) {
            item.createDt = item.createDt?.let { ProviderUtilities().toTimezone(it) }
            item.updateDt = item.updateDt?.let { ProviderUtilities().toTimezone(it) }
        }

        return WFProcessList
    }

    /**
     * 프로세스 신규 기본 정보 등록.
     */
    fun insertProcess(WFProcessDto: WFProcessDto): String {
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
