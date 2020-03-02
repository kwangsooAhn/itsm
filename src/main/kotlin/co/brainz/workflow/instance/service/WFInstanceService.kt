package co.brainz.workflow.instance.service

import co.brainz.workflow.instance.constants.InstanceConstants
import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.dto.TicketViewDto
import co.brainz.workflow.instance.entity.InstanceMstEntity
import co.brainz.workflow.instance.repository.InstanceMstRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WFInstanceService(private val instanceMstRepository: InstanceMstRepository) {

    /**
     * Instance Create.
     *
     * @param instanceDto
     * @return InstanceMstEntity
     */
    fun createInstance(instanceDto: InstanceDto): InstanceMstEntity {
        val instanceMstEntity = InstanceMstEntity(
                instanceId = "",
                instanceStatus = instanceDto.instanceStatus?:InstanceConstants.Status.RUNNING.code,
                processId = instanceDto.processId,
                instanceStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return instanceMstRepository.save(instanceMstEntity)
    }

    /**
     * Instance Complete.
     *
     * @param instanceDto
     */
    fun completeInstance(instanceId: String) {
        val instanceMstEntity = instanceMstRepository.findInstanceMstEntityByInstanceId(instanceId)
        if (instanceMstEntity.isPresent) {
            instanceMstEntity.get().instanceStatus = InstanceConstants.Status.FINISH.code
            instanceMstEntity.get().instanceEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            instanceMstRepository.save(instanceMstEntity.get())
        }
    }

    /**
     * Process Instance List.
     *
     * @param userKey userKey
     * @param status token status
     * @return List<InstanceViewDto>
     */
    fun getInstances(userKey: String, status: String): List<TicketViewDto> {
        //TODO: (assignee_id === userKey && token_status === status) 리스트 조회
        //임시 데이터.
        val instanceList = mutableListOf<TicketViewDto>()
        instanceList.add(TicketViewDto(
                ticketId = "40288a9170896b10017089902c560006",
                ticketName = "장애신고 문서",
                ticketDesc = "임시로 작성된 문서입니다.",
                userKey = "이소현", // 임시. userKey return 해야함.
                createDt = LocalDateTime.now()
        ))
        instanceList.add(TicketViewDto(
                ticketId = "40288a9170896b10017089902c560011",
                ticketName = "단순문의 문서",
                ticketDesc = "테스트111.",
                userKey = "이소현", // 임시. userKey return 해야함.
                createDt = LocalDateTime.now().minusDays(10)
        ))
        instanceList.add(TicketViewDto(
                ticketId = "40288ab770999f6b017099a066140000",
                ticketName = "장애신고 문서",
                ticketDesc = "임시로 작성된 문서입니다.",
                userKey = "관리자", // 임시. userKey return 해야함.
                createDt = LocalDateTime.now().minusMonths(5)
        ))
        return instanceList
    }
}