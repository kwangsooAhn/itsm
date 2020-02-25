package co.brainz.workflow.process.service

import co.brainz.workflow.process.constants.ProcessConstants
import co.brainz.workflow.process.dto.WFElementDto
import co.brainz.workflow.process.dto.WFProcessDto
import co.brainz.workflow.process.dto.WFProcessRestDto
import co.brainz.workflow.process.mapper.ProcessMstMapper
import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.entity.ProcessMstEntity
import co.brainz.workflow.process.mapper.ProcessMstMapper
import co.brainz.workflow.process.repository.ProcessMstRepository
import org.mapstruct.factory.Mappers
import org.springframework.security.core.context.SecurityContextHolder
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WFProcessService(private val processMstRepository: ProcessMstRepository) {

    private val processMstMapper = Mappers.getMapper(ProcessMstMapper::class.java)

    private val processMstMapper: ProcessMstMapper = Mappers.getMapper(ProcessMstMapper::class.java)

    /**
     * 프로세스 목록 조회
     */
    fun selectProcessList(search: String): MutableList<WFProcessDto> {
        val processDtoList = mutableListOf<WFProcessDto>()
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
            val wfProcessDto = processMstMapper.toWFProcessDto(it)
            wfProcessDto.enabled = enabled
            processDtoList.add(wfProcessDto)
        }
        return processDtoList
    }

    /**
     * 프로세스 조회
     */
    fun selectProcess(processId: String): WFProcessRestDto {
        val processMstEntity = processMstRepository.findByProcId(processId)

        val wfProcessDto = processMstMapper.toWFProcessDto(processMstEntity)

        val wfElementDto = mutableListOf<WFElementDto>()
        for (elementMstEntity in processMstEntity.elementMstEntity.orEmpty()) {
            wfElementDto.add(processMstMapper.toWFElementDto(elementMstEntity))
        }

        return WFProcessRestDto(wfProcessDto, wfElementDto)
    }

    /**
     * 프로세스 신규 등록
     */
    fun insertProcess(WFProcessDto: WFProcessDto): String {
        //TODO DB에 저장.
        val userName: String = SecurityContextHolder.getContext().authentication.name //사용자 이름
        val status = ProcessConstants.Status.EDIT.code // 등록 시 프로세스 상태
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
