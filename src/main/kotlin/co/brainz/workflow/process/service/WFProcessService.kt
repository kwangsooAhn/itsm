package co.brainz.workflow.process.service

import co.brainz.workflow.process.constants.ProcessConstants
import co.brainz.workflow.process.dto.WFProcessDto
import co.brainz.workflow.process.dto.WFProcessRestDto
import co.brainz.workflow.process.mapper.ProcessMstMapper
import co.brainz.workflow.process.repository.ProcessMstRepository
import org.mapstruct.factory.Mappers
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class WFProcessService(private val processMstRepository: ProcessMstRepository) {

    private val processMstMapper = Mappers.getMapper(ProcessMstMapper::class.java)

    /**
     * 프로세스 목록 조회
     */
    fun selectProcessList(search: String): MutableList<WFProcessDto> {
        val processDtoList = mutableListOf<WFProcessDto>()
        val procMstList = if (search.isEmpty()) {
            processMstRepository.findAll()
        } else {
            val word = "%$search%"
            processMstRepository.findByProcNameLikeOrProcDescLike(word, word)
        }

        procMstList.forEach {
            val enabled = when (it.procStatus) {
                ProcessConstants.Status.EDIT.code, ProcessConstants.Status.SIMULATION.code -> true
                else -> false
            }
            val wfProcessDto = processMstMapper.toWFProcessDto(it)
            wfProcessDto.enabled = enabled
            processDtoList.add(wfProcessDto)
        }
        return processDtoList
    }

    /**
     * 프로세스 조회
     */
    fun selectProcess(processId: String) {
        //return WFProcessDto(processId="", processName = "", processStatus="")
    }

    /**
     * 프로세스 신규 등록
     */
    fun insertProcess(WFProcessDto: WFProcessDto): String {
        //TODO DB에 저장.
        val userName: String = SecurityContextHolder.getContext().authentication.name //사용자 이름
        val status = ProcessConstants.Status.EDIT.code // 등록 시 프로세스 상태

        //등록된 process_id return
        return "test8cbdd784401aaad6d310df85ac2d"
    }

    /**
     * 프로세스 수정
     */
    fun updateProcess(wfProcessRestDto: WFProcessRestDto): String {

        val processId = wfProcessRestDto.process?.id ?: ""
        val processMstEntity = processMstRepository.findByProcId(processId)

        TODO("업데이트 구문 추가")

        return ""
    }

    /**
     * 프로세스 삭제
     */
    fun deleteProcess(processId: String) {
        //TODO DB에서 삭제.
    }
}
