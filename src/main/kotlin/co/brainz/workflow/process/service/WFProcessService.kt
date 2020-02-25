package co.brainz.workflow.process.service

import co.brainz.workflow.process.constants.ProcessConstants
import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.entity.ProcessMstEntity
import co.brainz.workflow.process.mapper.ProcessMstMapper
import co.brainz.workflow.process.repository.ProcessMstRepository
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WFProcessService(private val processMstRepository: ProcessMstRepository) {

    private val processMstMapper: ProcessMstMapper = Mappers.getMapper(ProcessMstMapper::class.java)

    /**
     * 프로세스 데이터 조회.
     */
    fun selectProcessList(search: String): MutableList<ProcessDto> {
        val processDtoList = mutableListOf<ProcessDto>()
        val procMstList = if (search.isEmpty()) {
            processMstRepository.findAll()
        } else {
            val word = "%$search%"
            processMstRepository.findByProcessNameLikeOrProcessDescLike(word, word)
        }
        procMstList.forEach {
            val enabled = when (it.processStatus) {
                ProcessConstants.Status.EDIT.code, ProcessConstants.Status.SIMULATION.code -> true
                else -> false
            }
            processDtoList.add(
                    ProcessDto(
                            it.processId,
                            it.processName,
                            it.processDesc,
                            it.processStatus,
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
    fun getProcess(processId: String): ProcessDto {
        val processMstEntity = processMstRepository.findProcessMstEntityByProcessId(processId)
        return ProcessDto(
                processId = processMstEntity.processId,
                processName = processMstEntity.processName,
                processDesc = processMstEntity.processDesc,
                processStatus = processMstEntity.processStatus,
                createUserKey = processMstEntity.createUserKey,
                createDt = processMstEntity.createDt,
                formId = processMstEntity.formMstEntity?.formId
        )
    }

    /**
     * 프로세스 신규 등록.
     */
    fun insertProcess(processDto: ProcessDto): ProcessDto {
        processDto.processStatus = ProcessConstants.Status.EDIT.code // 등록 시 프로세스 상태
        var processMstEntity: ProcessMstEntity = processMstRepository.save(processMstMapper.toProcessMstEntity(processDto))

        return ProcessDto(
                processMstEntity.processId,
                processMstEntity.processName,
                processMstEntity.processDesc,
                processMstEntity.processStatus,
                "",
                "",
                processMstEntity.createDt,
                "",
                processMstEntity.updateDt,
                "",
                false)
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): Boolean {
        val processMstEntity = processMstRepository.findProcessMstEntityByProcessId(processId)
        if (processMstEntity.processStatus.equals(ProcessConstants.Status.PUBLISH)
                || processMstEntity.processStatus.equals(ProcessConstants.Status.DESTROY)) {
            return false
        } else {
            processMstRepository.deleteById(processMstEntity.processId)
        }
        return true
    }

    /**
     * 프로세스 정보 변경.
     */
    fun updateProcess(processDto: ProcessDto): Boolean {
        if (processDto.processStatus.equals(ProcessConstants.Status.PUBLISH)
                || processDto.processStatus.equals(ProcessConstants.Status.DESTROY)) {
            return false
        } else {
            var processMstEntity: ProcessMstEntity = processMstMapper.toProcessMstEntity(processDto)
            processMstRepository.save(processMstEntity)
        }
        return true
    }
}
