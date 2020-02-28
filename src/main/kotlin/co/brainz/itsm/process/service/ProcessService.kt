package co.brainz.itsm.process.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.provider.ProviderProcess
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.dto.ProcessDto
import co.brainz.workflow.process.dto.WfJsonProcessDto
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
class ProcessService(private val providerProcess: ProviderProcess) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 데이터 조회.
     */
    fun findProcessList(search: String): List<WfJsonProcessDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("search", search)
        val responseBody = providerProcess.getProcesses(params)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val WfJsonProcessList: List<WfJsonProcessDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, WfJsonProcessDto::class.java))
        for (item in WfJsonProcessList) {
            item.createDt = item.createDt?.let { ProviderUtilities().toTimezone(it) }
            item.updateDt = item.updateDt?.let { ProviderUtilities().toTimezone(it) }
        }

        return WfJsonProcessList
    }

    /**
     * 프로세스 신규 등록
     */
    fun createProcess(processDto: ProcessDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        processDto.createUserKey = aliceUserDto.userKey
        processDto.createDt =  ProviderUtilities().toGMT(LocalDateTime.now())
        val responseBody: String = providerProcess.createProcess(processDto)
        return when (responseBody.isNotEmpty()) {
            true -> {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                val dataDto = mapper.readValue(responseBody, ProcessDto::class.java)
                dataDto.processId
            }
            false -> ""
        }
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): Boolean {
        return  providerProcess.deleteProcess(processId)
    }
}
