package co.brainz.workflow.process.service

import co.brainz.workflow.process.constants.ProcessConstants
import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.repository.ProcessMstRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class WFProcessService(private val processMstRepository: ProcessMstRepository) {

    /**
     * 프로세스 데이터 조회.
     */
    fun selectProcessList(search: String): MutableList<ProcessDto> {
        val processDtoList = mutableListOf<ProcessDto>()
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
            processDtoList.add(
                    ProcessDto(
                            it.procId,
                            it.procName,
                            it.procDesc,
                            it.procStatus,
                            it.formMstEntity?.formId,
                            it.formMstEntity?.formName,
                            it.createDt,
                            it.createUserKey,
                            it.updateDt,
                            it.updateUserKey,
                            //it.aliceUserEntity!!.userName,
                            enabled
                    )
            )
        }
        return processDtoList
    }

    /**
     * Search Process.
     *
     * @param procId
     * @return ProcessDto
     */
    fun getProcess(procId: String): ProcessDto {
        val processMstEntity = processMstRepository.findProcessMstEntityByProcId(procId)
        return ProcessDto(
                processId = processMstEntity.procId,
                processName = processMstEntity.procName,
                processDesc = processMstEntity.procDesc,
                processStatus = processMstEntity.procStatus,
                createUserKey = processMstEntity.createUserKey,
                createDt = processMstEntity.createDt,
                formId = processMstEntity.formMstEntity?.formId
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
